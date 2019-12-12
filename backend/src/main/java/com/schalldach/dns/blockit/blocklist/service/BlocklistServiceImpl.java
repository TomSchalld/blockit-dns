/*
 * Copyright (c) 2019.
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Thomas Schalldach software@schalldach.com
 */

package com.schalldach.dns.blockit.blocklist.service;

import com.schalldach.dns.blockit.blocklist.data.BlocklistData;
import com.schalldach.dns.blockit.blocklist.data.BlocklistRegistry;
import com.schalldach.dns.blockit.blocklist.data.BlocklistRepo;
import com.schalldach.dns.blockit.blocklist.dto.BlocklistCreateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by @author Thomas Schalldach on 12/12/2019 software@thomas-schalldach.de.
 */
@Service
@Transactional
@Slf4j
public class BlocklistServiceImpl implements BlocklistService {

    @Autowired
    private BlocklistRepo blocklistRepo;

    @Autowired
    private BlocklistDownloader blocklistDownloader;

    @Autowired
    private BlocklistParser blocklistParser;


    @Override
    public List<BlocklistRegistry> findAll() {
        return blocklistRepo.findAll();
    }

    @Override
    public void deleteByUrl(String url) {
        blocklistRepo.findByUrl(url).ifPresent(blocklistRepo::delete);
    }

    @Async("asyncWorker")
    @Override
    public void create(BlocklistCreateDto dto) {
        final byte[] blocklist = blocklistDownloader.fetch(dto.getUrl());
        log.info("Fetched data with content length {}", blocklist.length);
        final Collection<BlocklistData> blocklistEntries = blocklistParser.parse(blocklist);
        final BlocklistRegistry entity = new BlocklistRegistry();
        entity.setUrl(dto.getUrl());
        entity.setActive(dto.isActive());
        entity.setDataSet(new HashSet<>(blocklistEntries));
        blocklistRepo.save(entity);
    }
}
