/*
 * Copyright (c) 2019.
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Thomas Schalldach software@schalldach.com
 */

package com.schalldach.dns.blockit.blocklist.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by @author Thomas Schalldach on 12/12/2019 software@thomas-schalldach.de.
 */
@Repository
public interface BlocklistDataRepo extends JpaRepository<BlocklistData, Long> {
}
