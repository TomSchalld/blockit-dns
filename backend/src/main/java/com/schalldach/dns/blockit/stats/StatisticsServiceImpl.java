package com.schalldach.dns.blockit.stats;

import com.schalldach.dns.blockit.logging.service.LogService;
import com.schalldach.dns.blockit.stats.api.StatisticsService;
import com.schalldach.dns.blockit.stats.data.DailyStatisticsRepo;
import com.schalldach.dns.blockit.stats.data.HourlyKeyValueStat;
import com.schalldach.dns.blockit.stats.data.KeyValueStat;
import com.schalldach.dns.blockit.stats.data.QueryDto;
import com.schalldach.dns.blockit.stats.data.StatisticsRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by @author Thomas Schalldach on 26/12/2019 software@thomas-schalldach.de.
 */
@Service
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {

    private static final String TOTAL_NUM_CACHEHITS = "total.num.cachehits";
    private static final String TOTAL_NUM_CACHEMISS = "total.num.cachemiss";
    private static final String TOTAL_NUM_QUERIES = "total.num.queries";
    private static final String NUM_QUERIES = "num.queries";

    @Autowired
    private DailyStatisticsRepo dailyStatisticsRepo;

    @Autowired
    private StatisticsRepo statisticsRepo;

    @Autowired
    private LogService logService;


    @Override
    public List<QueryDto> getDailyQueries() {
        final Date date = new Date();
        final Date dateTime = DateUtils.truncate(date, Calendar.DATE);
        final long hour = DateUtils.getFragmentInHours(date, Calendar.DATE);
        final List<QueryDto> currentData = statisticsRepo.getAllByCreationTimeAndDay(hour, dateTime)
                .stream()
                .flatMap(dataPoint -> dataPoint.getKeyValueStats().stream())
                .filter(keyValueStat -> keyValueStat.getKey().endsWith(NUM_QUERIES))
                .collect(Collectors.toMap(KeyValueStat::getKey, keyValueStat -> ((long) Double.parseDouble(keyValueStat.getValue())), Long::sum))
                .entrySet()
                .stream()
                .map(entry -> new QueryDto(DateUtils.truncate(date, Calendar.HOUR), entry.getValue(), entry.getKey()))
                .collect(Collectors.toList());


        currentData.addAll(dailyStatisticsRepo.findAll().stream()
                .map(dataPoint -> dataPoint.
                        getKeyValueStats()
                        .stream()
                        .filter(keyValueStat -> keyValueStat.getKey().endsWith(NUM_QUERIES))
                        .map(hourlyKeyValueStat -> new QueryDto(dataPoint.getCreationDate(), Long.valueOf(hourlyKeyValueStat.getValue()), hourlyKeyValueStat.getKey())))
                .flatMap(queryDtoStream -> queryDtoStream)
                .collect(Collectors.toList()));
        currentData.sort(Comparator.comparingLong(queryDto -> queryDto.getPoint().getTime()));
        return currentData;

    }

    @Override
    public Long getDailyQueryCount() {
        return getStatisticsValue(TOTAL_NUM_QUERIES);
    }

    private Long getStatisticsValue(String s) {
        final Date date = new Date();
        final Date dateTime = DateUtils.truncate(date, Calendar.DATE);
        final long hour = DateUtils.getFragmentInHours(date, Calendar.DATE);
        final String statisticsKey = s;
        return statisticsRepo.getAllByCreationTimeAndDay(hour, dateTime)
                .stream()
                .flatMap(hourlyDataPoint -> hourlyDataPoint.getKeyValueStats().stream())
                .filter(keyValueStat -> keyValueStat.getKey().equals(statisticsKey))
                .map(KeyValueStat::getValue)
                .map(Long::valueOf)
                .reduce(0L, Long::sum)
                + dailyStatisticsRepo.findAll()
                .stream()
                .flatMap(hourlyDataPoint -> hourlyDataPoint.getKeyValueStats().stream())
                .filter(keyValueStat -> keyValueStat.getKey().equals(statisticsKey))
                .map(HourlyKeyValueStat::getValue)
                .map(Long::valueOf)
                .reduce(0L, Long::sum);
    }

    @Override
    public Double getDailyAverageResponseTime() {
        return logService.getAverageQueryTime();
    }

    @Override
    public Long getDailyCacheHits() {
        return getStatisticsValue(TOTAL_NUM_CACHEHITS);
    }

    @Override
    public Long getDailyCacheMisses() {
        return getStatisticsValue(TOTAL_NUM_CACHEMISS);
    }


}
