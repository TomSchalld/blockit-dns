package com.schalldach.dns.blockit.logging.service;

import com.schalldach.dns.blockit.logging.service.LogAware.Log;

/**
 * Created by @author Thomas Schalldach on 01/01/2020 software@thomas-schalldach.de.
 */
public interface LogService {
    Log getLog();

    Log getRepliesLog();

    Log getRefusedLog();

    long getRefusedCount();

    long getRepliesCount();

    double getAverageQueryTime();
}
