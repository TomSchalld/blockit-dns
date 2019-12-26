package com.schalldach.dns.blockit.stats.api;

import lombok.Data;

import java.util.Collection;

/**
 * Created by @author Thomas Schalldach on 26/12/2019 software@thomas-schalldach.de.
 */
public interface UpStatusAware {
    UpStatus getUpStatus();

    @Data
    final class UpStatus {
        private final Collection<String> healthStatus;
    }
}
