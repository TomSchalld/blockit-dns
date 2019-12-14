package com.schalldach.dns.blockit.control;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

/**
 * Created by @author Thomas Schalldach on 14/12/2019 software@thomas-schalldach.de.
 */
@Service
@ApplicationScope
@Slf4j
public class UnboundControl implements ServiceControl {


    @Override
    public void applyChanges() {

    }
}
