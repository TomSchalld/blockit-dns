package com.schalldach.dns.blockit.control;

import java.util.List;

/**
 * Created by @author Thomas Schalldach on 14/12/2019 software@thomas-schalldach.de.
 */
public interface ServiceControl {
    List<String> execRemoteCommand(RemoteCommand command, String... args);

    void applyChanges();
}
