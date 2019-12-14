package com.schalldach.dns.blockit.blocklist.service.impl;

import com.schalldach.dns.blockit.blocklist.data.BlocklistData;
import com.schalldach.dns.blockit.blocklist.data.BlocklistRegistry;
import com.schalldach.dns.blockit.blocklist.data.BlocklistRepo;
import com.schalldach.dns.blockit.blocklist.service.api.BlocklistDownloader;
import com.schalldach.dns.blockit.blocklist.service.api.BlocklistExporter;
import com.schalldach.dns.blockit.blocklist.service.api.BlocklistRefreshService;
import com.schalldach.dns.blockit.control.ServiceControl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    private ServiceControl serviceControl;


    @Override
    public void refresh() {
        downloadActive();
        blocklistExporter.exportActive();
        serviceControl.applyChanges();
    }

    @Transactional
    protected void downloadActive() {
        final Optional<List<BlocklistRegistry>> active = blocklistRepo.findAllActive();
        active.ifPresent(blocklistRegistries -> blocklistRegistries.forEach(blocklistRegistry -> {
            final byte[] blocklist = blocklistDownloader.fetch(blocklistRegistry.getUrl());
            blocklistRegistry.setData(new BlocklistData(blocklist));
        }));
    }


}
