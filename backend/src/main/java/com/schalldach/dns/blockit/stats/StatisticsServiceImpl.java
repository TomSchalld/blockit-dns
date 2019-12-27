package com.schalldach.dns.blockit.stats;

import com.schalldach.dns.blockit.stats.api.StatisticsService;
import com.schalldach.dns.blockit.stats.data.DataPoint;
import com.schalldach.dns.blockit.stats.data.KeyValueStat;
import com.schalldach.dns.blockit.stats.data.QueryDto;
import com.schalldach.dns.blockit.stats.data.StatisticsRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by @author Thomas Schalldach on 26/12/2019 software@thomas-schalldach.de.
 */
@Service
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private StatisticsRepo statisticsRepo;


    @Override
    public List<QueryDto> getDailyQueries() {

        final List<DataPoint> dailyDataPoints = statisticsRepo.getAllByCreationDate(DateUtils.truncate(new Date(), Calendar.DATE));
        return dailyDataPoints.stream()/*.map(dataPoint -> {
            final List<KeyValueStat> keyValueStats = dataPoint.getKeyValueStats().stream().filter(keyValueStat -> keyValueStat.getKey().equals("total.num.queries")).collect(Collectors.toList());
            return new DataPoint(null,DateUtils.truncate(dataPoint.getCreationDate(),Calendar.HOUR),keyValueStats,0L);
        }).*/

                .map(dataPoint -> {
                    final Date creationDate = dataPoint.getCreationDate();
                    final String stat = dataPoint.getKeyValueStats().stream().filter(keyValueStat -> keyValueStat.getKey().equals("total.num.queries")).map(KeyValueStat::getValue).findFirst().orElse(null);
                    return new QueryDto(creationDate, stat != null ? Long.valueOf(stat) : null);
                }).collect(Collectors.toList());

    }

    @Override
    public Long getDailyQueryCount() {
        final List<DataPoint> dailyDataPoints = statisticsRepo.getAllByCreationDate(DateUtils.truncate(new Date(), Calendar.DATE));
        return dailyDataPoints.stream().map(dataPoint -> {
            return dataPoint.getKeyValueStats().stream()
                    .filter(keyValueStat -> keyValueStat.getKey().equals("total.num.queries"))
                    .map(KeyValueStat::getValue)
                    .map(Long::valueOf)
                    .reduce(0L, Long::sum);
        }).reduce(0L, Long::sum);
    }


}
