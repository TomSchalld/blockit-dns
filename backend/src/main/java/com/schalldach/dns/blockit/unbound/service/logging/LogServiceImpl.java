package com.schalldach.dns.blockit.unbound.service.logging;

import com.schalldach.dns.blockit.logging.service.LogAware.Log;
import com.schalldach.dns.blockit.logging.service.LogEntry;
import com.schalldach.dns.blockit.logging.service.LogService;
import com.schalldach.dns.blockit.unbound.UnboundContext;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.schalldach.dns.blockit.logging.service.LogAware.REPLY;

/**
 * Created by @author Thomas Schalldach on 01/01/2020 software@thomas-schalldach.de.
 */
@Service
public class LogServiceImpl implements LogService {

    public static final UnboundContext UNBOUND_CONTEXT = UnboundContext.getInstance();
    public static final String REFUSED = "REFUSED";
    public static final String NO_ERROR = "NOERROR";

    @Override
    public Log getLog() {
        return UNBOUND_CONTEXT.getLog();
    }

    @Override
    public Log getRepliesLog() {
        return new Log(getReplies().stream().filter(s -> s.contains(NO_ERROR)).collect(Collectors.toSet()));
    }

    @Override
    public Log getRefusedLog() {
        return new Log(getReplies().stream().filter(s -> s.contains(REFUSED)).collect(Collectors.toSet()));
    }

    @Override
    public long getRefusedCount() {
        return getReplies().stream().filter(s -> s.contains(REFUSED)).count();
    }

    @Override
    public long getRepliesCount() {
        return getReplies().stream().filter(s -> s.contains(NO_ERROR)).count();
    }

    @Override
    public double getAverageQueryTime() {
        return getReplies().stream()
                .map(s -> s.split(" "))
                .map(LogEntry::new)
                .map(LogEntry::getTime)
                .collect(Collectors.averagingDouble(aDouble -> aDouble));

    }

    private Collection<String> getReplies() {
        return UNBOUND_CONTEXT.getLog().getLog()
                .stream()
                .filter(s -> s.contains(REPLY))
                .map(s -> s.substring(s.lastIndexOf(REPLY)))
                .map(s -> s.replace(REPLY, ""))
                .map(String::trim)
                .collect(Collectors.toList());
    }


}
