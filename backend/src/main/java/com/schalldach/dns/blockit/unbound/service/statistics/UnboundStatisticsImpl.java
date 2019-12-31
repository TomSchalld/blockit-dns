package com.schalldach.dns.blockit.unbound.service.statistics;

import com.schalldach.dns.blockit.control.ServiceControl;
import com.schalldach.dns.blockit.stats.data.DailyKeyValueStatRepo;
import com.schalldach.dns.blockit.stats.data.DailyStatisticsRepo;
import com.schalldach.dns.blockit.stats.data.DataPoint;
import com.schalldach.dns.blockit.stats.data.HourlyDataPoint;
import com.schalldach.dns.blockit.stats.data.HourlyKeyValueStat;
import com.schalldach.dns.blockit.stats.data.KeyValueStat;
import com.schalldach.dns.blockit.stats.data.KeyValueStatRepo;
import com.schalldach.dns.blockit.stats.data.StatisticsRepo;
import com.schalldach.dns.blockit.unbound.service.control.UnboundCommands;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by @author Thomas Schalldach on 18/12/2019 software@thomas-schalldach.de.
 */
@Slf4j
@Transactional
public class UnboundStatisticsImpl implements UnboundStatistics {


    @Autowired
    private ServiceControl serviceControl;
    @Autowired
    private StatisticsRepo statisticsRepo;
    @Autowired
    private KeyValueStatRepo keyValueStatRepo;
    @Autowired
    private DailyKeyValueStatRepo dailyKeyValueStatRepo;
    @Autowired
    private DailyStatisticsRepo dailyStatisticsRepo;


    @Override
    public void updateCurrentStatistics() {
        final List<String> result = serviceControl.execRemoteCommand(UnboundCommands.STATS);
        if (log.isTraceEnabled()) {
            result.forEach(log::trace);
        }
        final DataPoint entity = new DataPoint();
        final List<KeyValueStat> stats = result.stream()
                .map(s -> s.split("="))
                .map(split -> KeyValueStat.builder().key(split[0]).value(split[1]).dataPoint(entity).build())
                .collect(Collectors.toUnmodifiableList());
        entity.setCreationDate(new Date());
        entity.setKeyValueStats(stats);
        statisticsRepo.save(entity);
    }

    @Override
    public void consolidate() {
        final Date lastHoursDateTime = DateUtils.addHours(DateUtils.truncate(new Date(), Calendar.HOUR), -1);
        final long lastHoursHour = DateUtils.getFragmentInHours(lastHoursDateTime, Calendar.DATE);
        final List<DataPoint> statsFromLastHour = statisticsRepo.getAllByCreationTimeAndDay(lastHoursHour, DateUtils.truncate(lastHoursDateTime, Calendar.DATE));
        final HourlyDataPoint hourlyStats = new HourlyDataPoint();
        hourlyStats.setCreationDate(lastHoursDateTime);
        final ArrayList<HourlyKeyValueStat> hourlyKeyValueStats = new ArrayList<>();
        hourlyStats.setKeyValueStats(hourlyKeyValueStats);
        final List<KeyValueStat> lastHoursKeyValueStats = statsFromLastHour.stream().flatMap(dataPoint -> dataPoint.getKeyValueStats().stream()).collect(Collectors.toList());
        lastHoursKeyValueStats.stream().filter(keyValueStat -> !keyValueStat.getKey().startsWith("time"))
                .collect(Collectors.toMap(KeyValueStat::getKey, keyValueStat -> ((long) Double.parseDouble(keyValueStat.getValue())), Long::sum))
                .forEach((s, aDouble) -> hourlyKeyValueStats.add(HourlyKeyValueStat.builder().dataPoint(hourlyStats).key(s)
                        .value(String.valueOf(aDouble)).build()));
        dailyStatisticsRepo.save(hourlyStats);
        keyValueStatRepo.deleteInBatch(lastHoursKeyValueStats);
        statisticsRepo.deleteInBatch(statsFromLastHour);
        cleanupRemains();
    }

    private void cleanupRemains() {
        final Date today = DateUtils.truncate(new Date(), Calendar.DATE);
        final List<HourlyDataPoint> all = dailyStatisticsRepo.findAll().stream().filter(hourlyDataPoint -> hourlyDataPoint.getCreationDate().before(today)).collect(Collectors.toList());
        all.forEach(hourlyDataPoint -> dailyKeyValueStatRepo.deleteInBatch(hourlyDataPoint.getKeyValueStats()));
        dailyStatisticsRepo.deleteInBatch(all);
        final Date lastHoursDateTime = DateUtils.addHours(DateUtils.truncate(new Date(), Calendar.HOUR), -1);
        final List<DataPoint> collect = statisticsRepo.findAll().stream().filter(dataPoint -> dataPoint.getCreationDate().before(lastHoursDateTime)).collect(Collectors.toList());
        collect.forEach(dataPoint -> keyValueStatRepo.deleteInBatch(dataPoint.getKeyValueStats()));
        statisticsRepo.deleteInBatch(collect);
    }
}
