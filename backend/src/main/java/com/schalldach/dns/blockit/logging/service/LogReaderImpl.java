package com.schalldach.dns.blockit.logging.service;

import com.schalldach.dns.blockit.logging.service.LogAware.Log;
import com.schalldach.dns.blockit.unbound.UnboundContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by @author Thomas Schalldach on 01/01/2020 software@thomas-schalldach.de.
 */
@Slf4j
@Service
public class LogReaderImpl implements LogReader, InitializingBean {


    public static final String[] COMMANDS = {"bash", "-c", "journalctl -u unbound -f --since today -o cat"};
    private final ProcessBuilder processBuilder = new ProcessBuilder();
    private Process journalCtl;
    private UnboundContext context = UnboundContext.getInstance();
    @Autowired
    @Qualifier("asyncWorker")
    private TaskExecutor taskExecutor;

    @Async("asyncWorker")
    public void readLogs() {
        processBuilder.command(COMMANDS);
        try {

            journalCtl = processBuilder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(journalCtl.getInputStream()))) {
                final List<String> logList = new ArrayList<>();
                context.setLog(new Log(logList));
                String line;
                while ((line = reader.readLine()) != null) {
                    logList.add(line);
                }
            }

            int exitCode = journalCtl.waitFor();

            log.info("Exited with error code : {}", exitCode);

        } catch (IOException | InterruptedException e) {
            log.warn("Error while journal reading", e);
        }

    }


    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Initiating unbound log reader");
        CompletableFuture.runAsync(this::readLogs, taskExecutor);

    }
}
