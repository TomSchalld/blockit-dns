package com.schalldach.dns.blockit.unbound.service;

/**
 * Created by @author Thomas Schalldach on 17/12/2019 software@thomas-schalldach.de.
 */
public interface UnboundService {


    void updateStatistics();

    void insertDefaultConfiguration();


    void doHealthCheck();

    void exportConfiguration(String configurationName);

    void reload();
}
