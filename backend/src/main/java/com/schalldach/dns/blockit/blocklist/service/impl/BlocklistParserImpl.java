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
                .filter(line -> !line.isEmpty())
                .collect(Collectors.toSet());
    }
}
