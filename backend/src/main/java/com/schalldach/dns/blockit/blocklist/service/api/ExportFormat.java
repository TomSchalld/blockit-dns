package com.schalldach.dns.blockit.blocklist.service.api;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by @author Thomas Schalldach on 12/12/2019 software@thomas-schalldach.de.
 */
public interface ExportFormat {

    byte[] map(final String data);

    void addFileHeader(OutputStream out) throws IOException;
}
