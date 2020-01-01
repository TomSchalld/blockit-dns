package com.schalldach.dns.blockit.logging.service;

import com.schalldach.dns.blockit.logging.service.LogAware.Log;
import com.schalldach.dns.blockit.unbound.UnboundContext;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by @author Thomas Schalldach on 01/01/2020 software@thomas-schalldach.de.
 */
@Service
public class LogServiceImpl implements LogService {

    public static final UnboundContext UNBOUND_CONTEXT = UnboundContext.getInstance();
    public static final String REFUSED = "REFUSED";
    public static final String NO_ERROR = "NOERROR";
    public static final String REPLY = "reply:";

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

    private Collection<String> getReplies() {
        return UNBOUND_CONTEXT.getLog().getLog().stream().filter(s -> s.contains(REPLY)).collect(Collectors.toList());
    }

    private Collection<LogEntry> getLogEntries() {
        return UNBOUND_CONTEXT.getLog().getLog()
                .stream()
                .map(s -> s.substring(s.indexOf(REPLY)).trim())
                .map(s -> s.split(" "))
                .map(LogEntry::new)
                .collect(Collectors.toList());

    }


}
