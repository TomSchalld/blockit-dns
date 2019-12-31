package com.schalldach.dns.blockit.stats.data;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by @author Thomas Schalldach on 18/12/2019 software@thomas-schalldach.de.
 */
public interface KeyValueStatRepo extends JpaRepository<KeyValueStat, Long> {


}
