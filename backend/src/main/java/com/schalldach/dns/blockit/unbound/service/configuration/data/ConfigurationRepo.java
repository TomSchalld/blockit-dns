package com.schalldach.dns.blockit.unbound.service.configuration.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by @author Thomas Schalldach on 18/12/2019 software@thomas-schalldach.de.
 */
public interface ConfigurationRepo extends JpaRepository<Configuration, Long> {
    Optional<Configuration> findByConfigurationName(String name);
}
