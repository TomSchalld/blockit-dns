package com.schalldach.dns.blockit.unbound.service.statistics;

import org.springframework.transaction.annotation.Transactional;

/**
 * Created by @author Thomas Schalldach on 18/12/2019 software@thomas-schalldach.de.
 */
public interface UnboundStatistics {
    @Transactional
    void updateCurrentStatistics();

    @Transactional
    void consolidate();
}
