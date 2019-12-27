package com.schalldach.dns.blockit.stats.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by @author Thomas Schalldach on 18/12/2019 software@thomas-schalldach.de.
 */
public interface StatisticsRepo extends JpaRepository<DataPoint, Long> {


    @Query(value = "select * from unbound_stats_data where date(creation_date) = :creationDate", nativeQuery = true)
    List<DataPoint> getAllByCreationDate(@Param("creationDate") Date creationDate);


}
