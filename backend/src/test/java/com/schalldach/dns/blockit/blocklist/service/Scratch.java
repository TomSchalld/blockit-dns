package com.schalldach.dns.blockit.blocklist.service;

import com.schalldach.dns.blockit.logging.service.LogAware.Log;
import com.schalldach.dns.blockit.logging.service.LogEntry;
import org.junit.Test;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.schalldach.dns.blockit.logging.service.LogAware.REPLY;

/**
 * Created by @author Thomas Schalldach on 02/01/2020 software@thomas-schalldach.de.
 */

public class Scratch {

    @Test
    public void isQuerySuspicious() {
        final String string = "Jan 02 11:25:36 debiandev unbound[518]: [518:2] reply: 192.168.188.53 eas.outlook.com. A IN NOERROR 0.140014 0 172";
        final String suspicious = "Jan 02 11:25:36 debiandev unbound[518]: [518:2] reply: 192.168.188.53 eas-ads.outlook.com. A IN NOERROR 0.140014 0 172";
        final String suspicious2 = "Jan 02 11:25:36 debiandev unbound[518]: [518:2] reply: 192.168.188.53 ads.outlook.com. A IN REFUSED 0.140014 0 172";
        System.out.println(string.substring(string.indexOf(REPLY)).replace(REPLY, "").trim());
        final Collection<LogEntry> logEntries = new Log(Stream.of(string, suspicious, suspicious2)
                .map(s -> s.substring(s.lastIndexOf(REPLY)))
                .map(s -> s.replace(REPLY, ""))
                .map(String::trim)
                .collect(Collectors.toList())).toLogEntries();
        logEntries.forEach(System.out::println);
    }

}
