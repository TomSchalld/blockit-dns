package com.schalldach.dns.blockit.control;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by @author Thomas Schalldach on 14/12/2019 software@thomas-schalldach.de.
 */
@Slf4j
@Service
public class UnboundControl implements ServiceControl {

    private Process unboundProcess;

    @EventListener
    public void onAppReady(ApplicationReadyEvent applicationReadyEvent) throws IOException {
        final long timestamp = applicationReadyEvent.getTimestamp();
        log.info("Application ready at [{}]", timestamp);
        final ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.inheritIO();
        processBuilder.command("ping", "192.168.188.22");
        log.info("Starting new process");
        unboundProcess = processBuilder.start();

    }

    @Override
    public void applyChanges() {
    }
}
