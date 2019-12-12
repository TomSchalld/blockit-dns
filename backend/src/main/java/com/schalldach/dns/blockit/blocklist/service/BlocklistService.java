/*
 * Copyright (c) 2019.
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Thomas Schalldach software@schalldach.com
 */

package com.schalldach.dns.blockit.blocklist.service;

import com.schalldach.dns.blockit.blocklist.data.BlocklistRegistry;
import com.schalldach.dns.blockit.blocklist.dto.BlocklistCreateDto;

import java.util.List;

/**
 * Created by @author Thomas Schalldach on 12/12/2019 software@thomas-schalldach.de.
 */
public interface BlocklistService {
    List<BlocklistRegistry> findAll();

    void deleteByUrl(String url);

    void create(BlocklistCreateDto dto);
}
