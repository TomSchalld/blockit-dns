package com.schalldach.dns.blockit.unbound.service;

import com.schalldach.dns.blockit.control.ServiceControl;
import com.schalldach.dns.blockit.unbound.UnboundContext;
import com.schalldach.dns.blockit.unbound.UnboundContext.UpStatus;
import com.schalldach.dns.blockit.unbound.data.DataPoint;
import com.schalldach.dns.blockit.unbound.data.KeyValueStat;
import com.schalldach.dns.blockit.unbound.data.StatisticsRepo;
import com.schalldach.dns.blockit.unbound.service.control.UnboundCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by @author Thomas Schalldach on 17/12/2019 software@thomas-schalldach.de.
 */
@Slf4j
public class UnboundServiceImpl implements UnboundService {


    @Autowired
    private ServiceControl serviceControl;

    @Autowired
    private StatisticsRepo statisticsRepo;

    private final UnboundContext context = UnboundContext.getInstance();


    @Override
    @Transactional
    public void updateStatistics() {
        final List<String> result = serviceControl.execRemoteCommand(UnboundCommands.STATS_NORESET);
        if (log.isTraceEnabled()) {
            result.forEach(log::trace);
        }
        final DataPoint entity = new DataPoint();
        final List<KeyValueStat> stats = result.stream()
                .filter(s -> s.startsWith("total"))
                .map(s -> s.split("="))
                .map(split -> KeyValueStat.builder().key(split[0]).value(split[1]).dataPoint(entity).build())
                .collect(Collectors.toUnmodifiableList());
        entity.setCreationDate(new Date());
        entity.setKeyValueStats(stats);
        statisticsRepo.save(entity);
    }

    @Override
    public void doHealthCheck() {
        final List<String> result = serviceControl.execRemoteCommand(UnboundCommands.STATUS);
        if (log.isTraceEnabled()) {
            result.forEach(log::debug);
        }
        context.setUpStatus(new UpStatus(result));
    }
}
