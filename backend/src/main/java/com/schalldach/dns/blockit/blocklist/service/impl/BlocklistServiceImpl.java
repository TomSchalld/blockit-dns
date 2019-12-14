/*
 * Copyright (c) 2019.
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Thomas Schalldach software@schalldach.com
 */

package com.schalldach.dns.blockit.blocklist.service.impl;

import com.schalldach.dns.blockit.blocklist.data.BlocklistData;
import com.schalldach.dns.blockit.blocklist.data.BlocklistRegistry;
import com.schalldach.dns.blockit.blocklist.data.BlocklistRepo;
import com.schalldach.dns.blockit.blocklist.dto.BlocklistCreateDto;
import com.schalldach.dns.blockit.blocklist.service.api.BlocklistDownloader;
import com.schalldach.dns.blockit.blocklist.service.api.BlocklistExporter;
import com.schalldach.dns.blockit.blocklist.service.api.BlocklistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by @author Thomas Schalldach on 12/12/2019 software@thomas-schalldach.de.
 */
@Service
@Transactional
@Slf4j
public class BlocklistServiceImpl implements BlocklistService {

    public static final double THOUSAND = 1000d;
    public static final double MILLION = THOUSAND * THOUSAND;

    @Autowired
    private BlocklistRepo blocklistRepo;

    @Autowired
    private BlocklistDownloader blocklistDownloader;

    @Autowired
    private BlocklistExporter blocklistExporter;


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
        log.debug("Fetched data with content length {}", blocklist.length);
        final BlocklistRegistry entity = new BlocklistRegistry();
        entity.setUrl(dto.getUrl());
        entity.setActive(dto.isActive());
        entity.setData(new BlocklistData(blocklist));
        log.debug("Starting to persist blocklist with [{}kb] for list [{}]", blocklist.length / THOUSAND, dto.getUrl());
        blocklistRepo.save(entity);
        log.debug("Completed persistence blocklist for list [{}]", dto.getUrl());
    }

    @Async("asyncWorker")
    @Override
    public synchronized void export() {
        blocklistExporter.exportActive();
    }


}
