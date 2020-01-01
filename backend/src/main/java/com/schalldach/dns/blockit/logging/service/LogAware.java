package com.schalldach.dns.blockit.logging.service;

import lombok.Data;

import java.util.Collection;

/**
 * Created by @author Thomas Schalldach on 01/01/2020 software@thomas-schalldach.de.
 */
public interface LogAware {


    Log getLog();

    @Data
    final class Log {
        private final Collection<String> log;
    }

}
