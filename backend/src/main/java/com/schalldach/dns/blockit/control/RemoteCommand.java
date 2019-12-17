package com.schalldach.dns.blockit.control;

/**
 * Created by @author Thomas Schalldach on 17/12/2019 software@thomas-schalldach.de.
 */
public interface RemoteCommand {
    String execute(String... args);
}
