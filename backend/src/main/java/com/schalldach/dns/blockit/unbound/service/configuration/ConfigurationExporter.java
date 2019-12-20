package com.schalldach.dns.blockit.unbound.service.configuration;

import com.schalldach.dns.blockit.unbound.service.configuration.data.Configuration;

/**
 * Created by @author Thomas Schalldach on 18/12/2019 software@thomas-schalldach.de.
 */
public interface ConfigurationExporter {
    void export(Configuration configuration);
}
