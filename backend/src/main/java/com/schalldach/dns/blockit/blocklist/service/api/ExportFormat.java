package com.schalldach.dns.blockit.blocklist.service.api;

/**
 * Created by @author Thomas Schalldach on 12/12/2019 software@thomas-schalldach.de.
 */
public interface ExportFormat {

    byte[] map(final String data);

}
