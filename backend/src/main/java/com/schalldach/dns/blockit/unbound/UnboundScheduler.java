package com.schalldach.dns.blockit.unbound;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by @author Thomas Schalldach on 17/12/2019 software@thomas-schalldach.de.
 */
@Slf4j
public class UnboundScheduler {

    @Autowired
    private UnboundService unboundService;


    @Scheduled(cron = "${statistic.refresh.cron}")
    public void runStatisticsJob() {
        log.debug("Running Statistics Job");
        unboundService.updateStatistics();
    }

    @Scheduled(cron = "${healthcheck.refresh.cron}")
    public void runHealthCheckJob() {
        log.debug("Running Statistics Job");
        unboundService.doHealthCheck();
    }


}
