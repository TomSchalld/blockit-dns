package com.schalldach.dns.blockit.unbound;

/**
 * Created by @author Thomas Schalldach on 17/12/2019 software@thomas-schalldach.de.
 */
public interface UnboundService {


    void updateStatistics();


    void doHealthCheck();
}
