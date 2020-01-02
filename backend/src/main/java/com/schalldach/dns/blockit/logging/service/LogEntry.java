package com.schalldach.dns.blockit.logging.service;


import lombok.Getter;
import lombok.ToString;

/**
 * Created by @author Thomas Schalldach on 01/01/2020 software@thomas-schalldach.de.
 */
@Getter
@ToString
public class LogEntry {
    public static final String AD_REGEX = "[^,]*ad[^,]*";
    private final String host;
    private final String queryAddress;
    private final String record;
    private final String status;
    private final double time;
    private final int messageSize;
    private final boolean suspicious;

    /*192.168.188.53 gateway.icloud.com. A IN NOERROR 0.276443 0 202*/
    public LogEntry(String[] logEntries) {
        this.host = logEntries[0];
        this.queryAddress = logEntries[1];
        this.record = logEntries[2];
        this.status = logEntries[4];
        this.time = Double.parseDouble(logEntries[5]);
        this.messageSize = Integer.parseInt(logEntries[7]);
        this.suspicious = this.queryAddress.matches(AD_REGEX);
    }
}
