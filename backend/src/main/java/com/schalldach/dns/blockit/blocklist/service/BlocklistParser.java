package com.schalldach.dns.blockit.blocklist.service;

import com.schalldach.dns.blockit.blocklist.data.BlocklistData;

import java.util.Collection;

/**
 * Created by @author Thomas Schalldach on 12/12/2019 software@thomas-schalldach.de.
 */
public interface BlocklistParser {
    Collection<BlocklistData> parse(byte[] blocklist);
}
