/*
 * Copyright (c) 2019.
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Thomas Schalldach software@schalldach.com
 */

package com.schalldach.dns.blockit.blocklist.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by @author Thomas Schalldach on 12/12/2019 software@thomas-schalldach.de.
 */
@Repository
public interface BlocklistRepo extends JpaRepository<BlocklistRegistry, Long> {

    Optional<BlocklistRegistry> findByUrl(String url);

    Optional<List<BlocklistRegistry>> findAllByActiveTrue();

    default Optional<List<BlocklistRegistry>> findAllActive() {
        return findAllByActiveTrue();
    }

    @Query(value = "select distinct blocklist from blocklist_registry_blocklist where blocklist_registry_id in (select id from blocklist_registry where active = true)", nativeQuery = true)
    Set<String> findAllActiveDomains();

    @Query(value = "select count(distinct blocklist) from blocklist_registry_blocklist", nativeQuery = true)
    Long countBlockedDomains();

}
