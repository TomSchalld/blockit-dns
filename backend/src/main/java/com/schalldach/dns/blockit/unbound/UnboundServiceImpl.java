package com.schalldach.dns.blockit.unbound;

import com.schalldach.dns.blockit.control.ServiceControl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by @author Thomas Schalldach on 17/12/2019 software@thomas-schalldach.de.
 */
@Slf4j
public class UnboundServiceImpl implements UnboundService {


    @Autowired
    private ServiceControl serviceControl;


    @Override
    public void updateStatistics() {
        final List<String> result = serviceControl.execRemoteCommand(UnboundCommands.STATS_NORESET);
        if (log.isTraceEnabled()) {
            result.forEach(log::trace);
        }
        final List<KeyValueStat> stats = result.stream().filter(s -> s.startsWith("total")).map(s -> {
            final String[] split = s.split("=");
            return new KeyValueStat(split[0], split[1]);
        }).collect(Collectors.toUnmodifiableList());


    }

    @Override
    public void doHealthCheck() {
        final List<String> result = serviceControl.execRemoteCommand(UnboundCommands.STATUS);
        if (log.isTraceEnabled()) {
            result.forEach(log::trace);
        }
    }
}
