package com.schalldach.dns.blockit.unbound.service.configuration.util;

import com.schalldach.dns.blockit.unbound.service.configuration.data.Configuration;
import com.schalldach.dns.blockit.unbound.service.configuration.data.FileMapping;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.schalldach.dns.blockit.unbound.service.configuration.ConfigurationExporterImpl.SERVER;


/**
 * Created by @author Thomas Schalldach on 18/12/2019 software@thomas-schalldach.de.
 */
@Slf4j
public final class ConfigurationParser {

    private static final String FORWARD_ADDR = "forward-addr";
    private static final String PRIVATE_DOMAIN = "private-domain";
    private static final String PRIVATE_ADDRESS = "private-address";
    private static final String ACCESS_CONTROL = "access-control";
    private static final String FORWARD_ZONE = "forward-zone:";
    private static final String NAME = "name: \".\"";

    private ConfigurationParser() {

    }


    public static List<byte[]> getBytes(final Configuration configuration) {
        final Method[] declaredMethods = configuration.getClass().getDeclaredMethods();
        return Stream.of(declaredMethods)
                .filter(method -> !method.getName().equalsIgnoreCase("equals"))
                .filter(method -> !method.getName().equalsIgnoreCase("hashcode"))
                .filter(method -> method.getName().startsWith("get"))
                .map(ConfigurationParser::mapToReflectionHelper)
                .filter(Objects::nonNull)
                .map(reflectionHelper -> {
                    try {
                        final Object result = reflectionHelper.getGetter().invoke(configuration);
                        if (result != null) {
                            final String fileMapping = reflectionHelper.getFileMapping();
                            if (result instanceof Collection) {
                                return handleCollections(reflectionHelper, result);
                            } else {
                                return Collections.singletonList((fileMapping + ": " + result).toLowerCase().trim());
                            }
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return new ArrayList<String>(0);
                })
                .flatMap(Collection::stream)
                .peek(log::trace)
                .map(s -> (s + System.lineSeparator()).getBytes(StandardCharsets.UTF_8))
                .collect(Collectors.toList());
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    private static List<String> handleCollections(ReflectionHelper reflectionHelper, Object result) {
        final String fileMapping = reflectionHelper.getFileMapping();
        switch (fileMapping) {
            case FORWARD_ADDR:
                final List<String> ret = new ArrayList<>();
                ret.add(FORWARD_ZONE);
                ret.add(NAME);
                ret.addAll((Collection<? extends String>) ((Collection) result).stream().map(o -> (FORWARD_ADDR + ": " + o)).collect(Collectors.toList()));
                ret.add(SERVER);
                return ret;
            case PRIVATE_DOMAIN:
                return ((Collection<? extends String>) result).stream().map(o -> PRIVATE_DOMAIN + ": \"" + o + "\"").collect(Collectors.toList());
            case PRIVATE_ADDRESS:
                return ((Collection<? extends String>) result).stream().map(o -> PRIVATE_ADDRESS + ": " + o).collect(Collectors.toList());
            case ACCESS_CONTROL:
                return ((Collection<? extends String>) result).stream().map(o -> ACCESS_CONTROL + ": " + o).collect(Collectors.toList());
            default:
                throw new IllegalStateException("Unexpected value: " + fileMapping);
        }
    }

    private static ReflectionHelper mapToReflectionHelper(Method method) {
        final Optional<ReflectionHelper> reflectionHelper = Stream.of(method.getDeclaringClass().getDeclaredFields())
                .filter(field -> field.getAnnotation(FileMapping.class) != null)
                .filter(field -> field.getName().equalsIgnoreCase(method.getName().substring(3)))
                .findAny().flatMap(field -> Optional.of(new ReflectionHelper(method, field.getAnnotation(FileMapping.class).value())));
        return reflectionHelper.orElse(null);
    }

    @Data
    private static class ReflectionHelper {
        private final Method getter;
        private final String fileMapping;
    }


}
