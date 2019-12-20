package com.schalldach.dns.blockit.blocklist.service.impl;


import com.schalldach.dns.blockit.blocklist.service.api.BlocklistParser;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by @author Thomas Schalldach on 12/12/2019 software@thomas-schalldach.de.
 */
@Component
public class BlocklistParserImpl implements BlocklistParser {


    @Override
    public Set<String> parse(byte[] blocklist) {
        final String readableList = new String(blocklist, StandardCharsets.UTF_8);
        return readableList.lines()
                .filter(line -> !line.isEmpty() && !line.startsWith("#") && !line.startsWith(":"))
                .map(s -> s.replace("127.0.0.1", ""))
                .map(s -> s.replaceAll("\\s+", ""))
                .map(s -> {
                    if (s.endsWith(".")) {
                        return s.substring(0, s.length() - 1).trim();
                    }
                    return s;
                })
                .filter(s -> s.matches("^([a-z0-9]+(-[a-z0-9]+)*\\.)+[a-z]{2,}$"))
                .map(String::toLowerCase).collect(Collectors.toSet());
    }
}
