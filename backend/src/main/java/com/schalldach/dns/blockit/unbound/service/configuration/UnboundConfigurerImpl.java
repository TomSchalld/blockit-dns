package com.schalldach.dns.blockit.unbound.service.configuration;

import com.schalldach.dns.blockit.unbound.service.configuration.data.Configuration;
import com.schalldach.dns.blockit.unbound.service.configuration.data.ConfigurationRepo;
import com.schalldach.dns.blockit.unbound.service.configuration.util.ConfigurationHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by @author Thomas Schalldach on 18/12/2019 software@thomas-schalldach.de.
 */
@Slf4j
public class UnboundConfigurerImpl implements UnboundConfigurer {

    public static final String DEFAULT = "default";

    @Autowired
    private ConfigurationRepo configurationRepo;
    @Autowired
    private ConfigurationExporter configurationExporter;


    @Override
    @Transactional
    public void insertDefault() {
        final Optional<Configuration> defaultConfiguration = configurationRepo.findByConfigurationName(DEFAULT);
        if (defaultConfiguration.isEmpty()) {
            configurationRepo.save(ConfigurationHelper.getDefaultConfig());
        }
    }

    @Transactional
    @Override
    public void export(String configurationName) {
        final Optional<Configuration> configuration = configurationRepo.findByConfigurationName(configurationName);
        configuration.ifPresent(configurationExporter::export);
    }

    private void exportConfiguration(final Configuration config) {

    }
}
