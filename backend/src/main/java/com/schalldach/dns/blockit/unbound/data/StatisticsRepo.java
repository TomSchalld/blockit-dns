package com.schalldach.dns.blockit.unbound.data;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by @author Thomas Schalldach on 18/12/2019 software@thomas-schalldach.de.
 */
public interface StatisticsRepo extends JpaRepository<DataPoint, Long> {
}
