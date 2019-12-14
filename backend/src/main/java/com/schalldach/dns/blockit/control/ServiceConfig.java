package com.schalldach.dns.blockit.control;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.ApplicationScope;

/**
 * Created by @author Thomas Schalldach on 14/12/2019 software@thomas-schalldach.de.
 */
@Configuration
public class ServiceConfig {


    @Bean
    @ApplicationScope
    public ServiceControl serviceControl() {
        return new UnboundControl();
    }

}
