package com.schalldach.dns.blockit.blocklist.service.impl;

import com.schalldach.dns.blockit.blocklist.service.api.BlocklistRefreshService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by @author Thomas Schalldach on 14/12/2019 software@thomas-schalldach.de.
 */
@Service
@Slf4j
public class BlocklistRefreshScheduler {

    @Autowired
    private BlocklistRefreshService blocklistRefreshService;


    @Scheduled(cron = "${blocklist.refresh.cron}")
    public void refresh() {
        log.info("Refreshing all active blocklists");
        blocklistRefreshService.refresh();
    }


}
