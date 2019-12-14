package com.schalldach.dns.blockit.blocklist.service;

import com.schalldach.dns.blockit.blocklist.data.BlocklistData;
import com.schalldach.dns.blockit.blocklist.dto.BlocklistCreateDto;

import java.util.Collection;

/**
 * Created by @author Thomas Schalldach on 13/12/2019 software@thomas-schalldach.de.
 */
public interface BlocklistPersister {
    void persist(BlocklistCreateDto dto, Collection<BlocklistData> blocklistEntries);
}
