package com.schalldach.dns.blockit.unbound;

import com.schalldach.dns.blockit.control.ServiceControl;
import com.schalldach.dns.blockit.unbound.service.UnboundScheduler;
import com.schalldach.dns.blockit.unbound.service.UnboundService;
import com.schalldach.dns.blockit.unbound.service.UnboundServiceImpl;
import com.schalldach.dns.blockit.unbound.service.configuration.ConfigurationExporter;
import com.schalldach.dns.blockit.unbound.service.configuration.ConfigurationExporterImpl;
import com.schalldach.dns.blockit.unbound.service.configuration.UnboundConfigurer;
import com.schalldach.dns.blockit.unbound.service.configuration.UnboundConfigurerImpl;
import com.schalldach.dns.blockit.unbound.service.control.UnboundControl;
import com.schalldach.dns.blockit.unbound.service.statistics.UnboundStatistics;
import com.schalldach.dns.blockit.unbound.service.statistics.UnboundStatisticsImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by @author Thomas Schalldach on 14/12/2019 software@thomas-schalldach.de.
 */
@Configuration
public class UnboundConfig {


    @Bean
    public ServiceControl serviceControl() {
        return new UnboundControl();
    }

    @Bean
    public UnboundScheduler unboundScheduler() {
        return new UnboundScheduler();
    }

    @Bean
    public UnboundService unboundService() {
        return new UnboundServiceImpl();
    }

    @Bean
    public UnboundStatistics unboundStatistics() {
        return new UnboundStatisticsImpl();
    }

    @Bean
    public UnboundConfigurer unboundConfigurer() {
        return new UnboundConfigurerImpl();
    }

    @Bean
    public ConfigurationExporter configurationExporter() {
        return new ConfigurationExporterImpl();
    }


}
