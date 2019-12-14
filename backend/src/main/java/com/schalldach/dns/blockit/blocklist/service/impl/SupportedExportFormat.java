package com.schalldach.dns.blockit.blocklist.service.impl;

import com.schalldach.dns.blockit.blocklist.service.api.ExportFormat;

import java.nio.charset.StandardCharsets;

/**
 * Created by @author Thomas Schalldach on 12/12/2019 software@thomas-schalldach.de.
 */
public enum SupportedExportFormat implements ExportFormat {
    UNBOUND {
        private static final String LOCAL_ZONE = "local-zone";
        private static final String REDIRECT = "redirect";
        public static final String LOCAL_DATA = "local-data: ";

        /* Expected output format
        local-zone: "1-1ads.com" redirect
        local-data: "1-1ads.com A 127.0.0.1"
        */
        @Override
        public byte[] map(final String data) {
            return new StringBuilder(LOCAL_ZONE)
                    .append(": ")
                    .append("\"")
                    .append(data)
                    .append("\" ")
                    .append(REDIRECT)
                    .append(System.lineSeparator())
                    .append(LOCAL_DATA)
                    .append("\"")
                    .append(data)
                    .append(" A ")
                    .append(LOCAL_HOST)
                    .append("\"")
                    .append(System.lineSeparator())
                    .toString().getBytes(StandardCharsets.UTF_8);
        }
    };

    public static final String LOCAL_HOST = "127.0.0.1";
}
