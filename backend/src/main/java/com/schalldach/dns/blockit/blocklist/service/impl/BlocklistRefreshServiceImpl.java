package com.schalldach.dns.blockit.blocklist.service.impl;

import com.schalldach.dns.blockit.blocklist.data.BlocklistRegistry;
import com.schalldach.dns.blockit.blocklist.data.BlocklistRepo;
import com.schalldach.dns.blockit.blocklist.service.api.BlocklistDownloader;
import com.schalldach.dns.blockit.blocklist.service.api.BlocklistExporter;
import com.schalldach.dns.blockit.blocklist.service.api.BlocklistParser;
import com.schalldach.dns.blockit.blocklist.service.api.BlocklistRefreshService;
import com.schalldach.dns.blockit.control.ServiceControl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by @author Thomas Schalldach on 14/12/2019 software@thomas-schalldach.de.
 */
@Service
@Slf4j
public class BlocklistRefreshServiceImpl implements BlocklistRefreshService {

    @Autowired
    private BlocklistRepo blocklistRepo;

    @Autowired
    private BlocklistDownloader blocklistDownloader;

    @Autowired
    private BlocklistExporter blocklistExporter;

    @Autowired
    private BlocklistParser blocklistParser;

    @Autowired
    private ServiceControl serviceControl;
    @Autowired
    @Qualifier("asyncWorker")
    private TaskExecutor taskExecutor;


    @Override
    public void refresh() {
        final long time = new Date().getTime();
        log.info("Starting blocklist refresh");
        final List<CompletableFuture<BlocklistRegistry>> tasks = downloadActive();
        CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new)).handleAsync((aVoid, throwable) -> {
            if (throwable != null) {
                log.warn("Something went wrong", throwable);
            }
            log.trace("Finished downloads after total of [{}]s", (new Date().getTime() - time) / 1000d);
            blocklistExporter.exportActive();
            log.debug("Finished export after total of [{}]s", (new Date().getTime() - time) / 1000d);
            serviceControl.applyChanges();
            log.info("Applied all changes after total of [{}]s", (new Date().getTime() - time) / 1000d);
            return aVoid;
        }, taskExecutor);

    }

    @Transactional
    protected List<CompletableFuture<BlocklistRegistry>> downloadActive() {
        final List<BlocklistRegistry> blocklistRegistries = blocklistRepo.findAllActive().orElse(Collections.emptyList());
        final List<CompletableFuture<BlocklistRegistry>> taskList = new ArrayList<>();
        for (BlocklistRegistry blocklistRegistry : blocklistRegistries) {
            final CompletableFuture<BlocklistRegistry> task = blocklistDownloader.fetch(blocklistRegistry.getUrl())
                    .handleAsync((bytes, throwable) -> {
                        if (throwable != null) {
                            log.warn("Something went wrong", throwable);
                        }
                        return blocklistParser.parse(bytes);
                    }, taskExecutor)
                    .handle((strings, throwable) -> {
                        if (throwable != null) {
                            log.warn("Something went wrong", throwable);
                        }
                        blocklistRegistry.setBlocklist(strings);
                        log.debug("Persisting blocklist data for list[{}]", blocklistRegistry.getUrl());
                        return blocklistRepo.save(blocklistRegistry);
                    }).handleAsync((blocklistRegistry1, throwable) -> {
                        if (throwable != null) {
                            log.warn("Something went wrong", throwable);
                        }
                        log.debug("Persisting blocklist data for list[{}] COMPLETED", blocklistRegistry1.getUrl());
                        return blocklistRegistry1;
                    }, taskExecutor);
            taskList.add(task);
        }
        return taskList;

    }


}
