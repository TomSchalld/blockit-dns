package com.schalldach.dns.blockit.unbound.service.configuration.data;

/**
 * Created by @author Thomas Schalldach on 18/12/2019 software@thomas-schalldach.de.
 */
public enum UnboundConfBool {
    YES, NO;

    public static UnboundConfBool fromBoolean(boolean val) {
        if (val) {
            return YES;
        }
        return NO;
    }

    public static boolean toBoolean(UnboundConfBool val) {
        switch (val) {
            case YES:
                return true;
            case NO:
                return false;
            default:
                throw new IllegalStateException("Unexpected value: " + val);
        }
    }


}
