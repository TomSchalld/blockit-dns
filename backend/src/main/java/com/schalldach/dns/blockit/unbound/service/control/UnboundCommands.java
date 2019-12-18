package com.schalldach.dns.blockit.unbound.service.control;

import com.schalldach.dns.blockit.control.RemoteCommand;

/**
 * Created by @author Thomas Schalldach on 17/12/2019 software@thomas-schalldach.de.
 */
public enum UnboundCommands implements RemoteCommand {
    RELOAD, STATS_NORESET, STATUS;

    private final static String MAGIC = "UBCT1";

    @Override
    public String execute(String... args) {
        final StringBuilder builder = new StringBuilder(MAGIC).append(" ").append(this.name().toLowerCase()).append(" ");
        for (String arg : args) {
            builder.append(arg);
            builder.append(" ");
        }
        return builder.toString().trim();
    }
}
