package com.schalldach.dns.blockit.unbound.service;

import com.schalldach.dns.blockit.control.ServiceControl;
import com.schalldach.dns.blockit.unbound.UnboundContext;
import com.schalldach.dns.blockit.unbound.UnboundContext.UpStatus;
import com.schalldach.dns.blockit.unbound.service.configuration.UnboundConfigurer;
import com.schalldach.dns.blockit.unbound.service.control.UnboundCommands;
import com.schalldach.dns.blockit.unbound.service.statistics.UnboundStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by @author Thomas Schalldach on 17/12/2019 software@thomas-schalldach.de.
 */
@Slf4j
public class UnboundServiceImpl implements UnboundService {

    private final UnboundContext context = UnboundContext.getInstance();

    @Autowired
    private ServiceControl serviceControl;

    @Autowired
    private UnboundStatistics unboundStatistics;

    @Autowired
    private UnboundConfigurer unboundConfigurer;


    @Override
    public void updateStatistics() {
        unboundStatistics.updateStatistics();
    }

    @PostConstruct
    @Override
    public void insertDefaultConfiguration() {
        unboundConfigurer.insertDefault();
    }

    @Override
    public void doHealthCheck() {
        final List<String> result = serviceControl.execRemoteCommand(UnboundCommands.STATUS);
        if (log.isTraceEnabled()) {
            result.forEach(log::debug);
        }
        context.setUpStatus(new UpStatus(result));
    }

    @Override
    public void exportConfiguration(String configurationName) {
        unboundConfigurer.export(configurationName);
    }
}
