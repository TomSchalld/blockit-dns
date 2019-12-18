package com.schalldach.dns.blockit.unbound.service.configuration.data;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by @author Thomas Schalldach on 18/12/2019 software@thomas-schalldach.de.
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface FileMapping {
    String value();
}
