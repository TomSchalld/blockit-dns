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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Value("${blocklist.export.format}")
    private SupportedExportFormat exportFormat;

    @Value("${blocklist.export.folder}")
    private Path exportPath;

    @Value("${blocklist.export.filename}")
    private String filename;


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

    @Async("asyncWorker")
    @Override
    public synchronized void export() {
        log.info("Starting blocklist export into format [{}] and into folder [{}]...", exportFormat, exportPath);
        final File exportFolder = exportPath.toFile();
        if (exportFolder.isDirectory()) {
            final File exportFile = Paths.get(exportPath.toString(), filename).toFile();
            try (OutputStream out = new FileOutputStream(exportFile)) {
                final Set<byte[]> collection = blocklistRepo.findAll()
                        .stream().filter(BlocklistRegistry::isActive)
                        .map(BlocklistRegistry::getDataSet)
                        .parallel().flatMap(blocklistData -> blocklistData.stream().map(exportFormat::map)
                                .map(s -> s.getBytes(StandardCharsets.UTF_8)))
                        .collect(Collectors.toSet());
                for (byte[] bytes : collection) {
                    out.write(bytes);
                }
            } catch (IOException e) {
                log.warn("Could not write Blocklist due to Error", e);
            }
        }


    }
}
