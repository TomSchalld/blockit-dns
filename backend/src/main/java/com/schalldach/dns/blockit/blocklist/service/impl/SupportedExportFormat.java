package com.schalldach.dns.blockit.blocklist.service.impl;

import com.schalldach.dns.blockit.blocklist.service.api.BlocklistExportFormat;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by @author Thomas Schalldach on 12/12/2019 software@thomas-schalldach.de.
 */
public enum SupportedExportFormat implements BlocklistExportFormat {
    UNBOUND {
        private static final String SERVER = "server:";
        private static final String LOCAL_ZONE = "local-zone: \"";
        private static final String REFUSE = "\" refuse";


        @Override
        public byte[] map(final String data) {
            return new StringBuilder(LOCAL_ZONE).append(data).append(REFUSE).append(System.lineSeparator()).toString().getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public void addFileHeader(OutputStream out) throws IOException {
            out.write((SERVER + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
        }
    };


}
