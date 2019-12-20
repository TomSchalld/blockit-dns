package com.schalldach.dns.blockit.unbound;

import com.schalldach.dns.blockit.unbound.service.control.UnboundCommands;
import com.schalldach.dns.blockit.unbound.service.control.UnboundControl;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Created by @author Thomas Schalldach on 17/12/2019 software@thomas-schalldach.de.
 */
class UnboundControlTest {

    private UnboundControl unboundControl = new UnboundControl();



    @Test
    @Ignore
    void execRemoteCommand() {
        unboundControl.setIp("192.168.188.90");
        unboundControl.setPort(8953);
        final List<String> stats = unboundControl.execRemoteCommand(UnboundCommands.STATS_NORESET);
        stats.forEach(System.out::println);
    }
}