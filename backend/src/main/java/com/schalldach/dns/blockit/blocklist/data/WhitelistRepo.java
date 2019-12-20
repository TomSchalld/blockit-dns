package com.schalldach.dns.blockit.blocklist.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by @author Thomas Schalldach on 19/12/2019 software@thomas-schalldach.de.
 */
@RepositoryRestResource(path = "whitelists")
public interface WhitelistRepo extends JpaRepository<CustomWhitelistedDomain, Long> {

    default Set<String> getAllDomains() {
        return findAll().stream().map(CustomWhitelistedDomain::getDomain).collect(Collectors.toSet());
    }

}
