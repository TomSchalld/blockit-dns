package com.schalldach.dns.blockit.blocklist.service;

import com.schalldach.dns.blockit.blocklist.data.BlocklistData;

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
        public String map(BlocklistData data) {
            final String entry = data.getEntry();
            return new StringBuilder(LOCAL_ZONE)
                    .append(": ")
                    .append("\"")
                    .append(entry)
                    .append("\" ")
                    .append(REDIRECT)
                    .append(System.lineSeparator())
                    .append(LOCAL_DATA)
                    .append("\"")
                    .append(entry)
                    .append(" A ")
                    .append(LOCAL_HOST)
                    .append("\"")
                    .append(System.lineSeparator())
                    .toString();
        }
    };

    public static final String LOCAL_HOST = "127.0.0.1";
}
