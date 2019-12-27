package com.schalldach.dns.blockit.stats.api;

import com.schalldach.dns.blockit.stats.data.QueryDto;

import java.util.List;

/**
 * Created by @author Thomas Schalldach on 26/12/2019 software@thomas-schalldach.de.
 */
public interface StatisticsService {


    List<QueryDto> getDailyQueries();

    Long getDailyQueryCount();
}
