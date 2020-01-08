package com.schalldach.dns.blockit.logging.service;

import lombok.Data;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by @author Thomas Schalldach on 01/01/2020 software@thomas-schalldach.de.
 */
public interface LogAware {


    Log getLog();

    public static final String REPLY = "reply:";


    @Data
    final class Log {
        private final Collection<String> log;

        public Collection<LogEntry> toLogEntries() {
            return log.stream()
                    .map(s -> s.split(" "))
                    .map(LogEntry::new)
                    .collect(Collectors.toSet());

        }
    }

}
