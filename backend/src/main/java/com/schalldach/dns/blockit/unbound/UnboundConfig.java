package com.schalldach.dns.blockit.unbound;

import com.schalldach.dns.blockit.control.ServiceControl;
import com.schalldach.dns.blockit.unbound.service.UnboundScheduler;
import com.schalldach.dns.blockit.unbound.service.UnboundService;
import com.schalldach.dns.blockit.unbound.service.UnboundServiceImpl;
import com.schalldach.dns.blockit.unbound.service.control.UnboundControl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.ApplicationScope;

/**
 * Created by @author Thomas Schalldach on 14/12/2019 software@thomas-schalldach.de.
 */
@Configuration
public class UnboundConfig {


    @Bean
    @ApplicationScope
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


}
