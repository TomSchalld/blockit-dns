package com.schalldach.dns.blockit.blocklist.service.api;

import java.util.Set;

/**
 * Created by @author Thomas Schalldach on 12/12/2019 software@thomas-schalldach.de.
 */
public interface BlocklistParser {
    Set<String> parse(byte[] blocklist);
}
