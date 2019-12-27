package com.schalldach.dns.blockit.stats;

import com.schalldach.dns.blockit.stats.api.StatisticsService;
import com.schalldach.dns.blockit.stats.api.UpStatusAware.UpStatus;
import com.schalldach.dns.blockit.stats.data.QueryDto;
import com.schalldach.dns.blockit.unbound.UnboundContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by @author Thomas Schalldach on 26/12/2019 software@thomas-schalldach.de.
 */
@RestController
@RequestMapping("/stats")
@Slf4j
public class StatsController {

    @Autowired
    private StatisticsService statisticsService;

    public static final UnboundContext UNBOUND_CONTEXT = UnboundContext.getInstance();

    @GetMapping("/health")
    public ResponseEntity<UpStatus> getHealth() {
        return new ResponseEntity<>(UNBOUND_CONTEXT.getUpStatus(), HttpStatus.OK);
    }

    @GetMapping("/queries/daily")
    public ResponseEntity<List<QueryDto>> getDaily() {
        return new ResponseEntity<>(statisticsService.getDailyQueries(), HttpStatus.OK);
    }

    @GetMapping("/queries/daily/count")
    public ResponseEntity<Long> getDailyCount() {
        return new ResponseEntity<>(statisticsService.getDailyQueryCount(), HttpStatus.OK);
    }


}
