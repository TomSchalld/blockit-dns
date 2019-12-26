package com.schalldach.dns.blockit.unbound;

import com.schalldach.dns.blockit.stats.api.UpStatusAware;

/**
 * Created by @author Thomas Schalldach on 18/12/2019 software@thomas-schalldach.de.
 */
public final class UnboundContext implements UpStatusAware {

    private static UnboundContext ctx;
    private final static Object lock = new Object();
    private UpStatus upStatus;


    private UnboundContext() {
    }

    public static UnboundContext getInstance() {
        if (ctx == null) {
            synchronized (lock) {
                if (ctx == null) {
                    ctx = new UnboundContext();
                }
            }
        }
        return ctx;
    }

    @Override
    public UpStatus getUpStatus() {
        return upStatus;
    }

    public void setUpStatus(UpStatus upStatus) {
        this.upStatus = upStatus;
    }


}
