/*
 * Copyright (c) 2019.
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Thomas Schalldach software@schalldach.com
 */

package com.schalldach.dns.blockit.blocklist.service.impl;

import com.schalldach.dns.blockit.blocklist.data.BlocklistRegistry;
import com.schalldach.dns.blockit.blocklist.data.BlocklistRepo;
import com.schalldach.dns.blockit.blocklist.dto.BlocklistCreateDto;
import com.schalldach.dns.blockit.blocklist.service.api.BlocklistDownloader;
import com.schalldach.dns.blockit.blocklist.service.api.BlocklistExporter;
import com.schalldach.dns.blockit.blocklist.service.api.BlocklistParser;
import com.schalldach.dns.blockit.blocklist.service.api.BlocklistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
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

    @Autowired
    private BlocklistParser blocklistParser;

    @Autowired
    @Qualifier("asyncWorker")
    private TaskExecutor taskExecutor;


    @Override
    public List<BlocklistRegistry> findAll() {
        return blocklistRepo.findAll();
    }

    @Override
    public List<BlocklistRegistry> findAllActive() {
        return blocklistRepo.findAllActive().orElse(Collections.emptyList());
    }

    @Override
    public void deleteByUrl(String url) {
        blocklistRepo.findByUrl(url).ifPresent(blocklistRepo::delete);
    }

    @Async("asyncWorker")
    @Override
    public void create(BlocklistCreateDto dto) {
        blocklistDownloader.fetch(dto.getUrl()).handleAsync((bytes, throwable) -> {
            if (throwable != null) {
                log.warn("something went wrong", throwable);
            }
            log.debug("Fetched data with content length {}", bytes.length);
            final BlocklistRegistry entity = new BlocklistRegistry();
            entity.setUrl(dto.getUrl());
            entity.setActive(dto.isActive());
            entity.setBlocklist(blocklistParser.parse(bytes));
            return entity;
        }, taskExecutor).handleAsync((blocklistRegistry, throwable) -> {
            if (throwable != null) {
                log.warn("something went wrong", throwable);
            }
            log.debug("Starting to persist blocklist with [{}] entries for list [{}]", blocklistRegistry.getBlocklist().size() / THOUSAND, dto.getUrl());
            blocklistRepo.save(blocklistRegistry);
            log.debug("Completed persistence blocklist for list [{}]", dto.getUrl());
            return Void.TYPE;
        }, taskExecutor);

    }

    @Async("asyncWorker")
    @Override
    public synchronized void export() {
        blocklistExporter.exportActive();
    }


    @Override
    public Long count() {
        return blocklistRepo.countBlockedDomains();
    }

    @Override
    public void deleteById(Long id) {
        blocklistRepo.deleteById(id);
    }

    @Override
    public void update(Long id, BlocklistCreateDto dto) {
        final BlocklistRegistry one = blocklistRepo.getOne(id);
        one.setUrl(dto.getUrl());
        one.setActive(dto.isActive());
    }


}
