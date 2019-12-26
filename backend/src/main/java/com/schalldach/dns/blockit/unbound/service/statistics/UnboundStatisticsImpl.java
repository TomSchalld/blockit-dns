package com.schalldach.dns.blockit.unbound.service.statistics;

import com.schalldach.dns.blockit.control.ServiceControl;
import com.schalldach.dns.blockit.unbound.service.control.UnboundCommands;
import com.schalldach.dns.blockit.unbound.service.statistics.data.DataPoint;
import com.schalldach.dns.blockit.unbound.service.statistics.data.KeyValueStat;
import com.schalldach.dns.blockit.unbound.service.statistics.data.StatisticsRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by @author Thomas Schalldach on 18/12/2019 software@thomas-schalldach.de.
 */
@Slf4j
@Transactional
public class UnboundStatisticsImpl implements UnboundStatistics {


    @Autowired
    private ServiceControl serviceControl;

    @Autowired
    private StatisticsRepo statisticsRepo;


    @Override
    public void updateStatistics() {
        final List<String> result = serviceControl.execRemoteCommand(UnboundCommands.STATS);
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
}
