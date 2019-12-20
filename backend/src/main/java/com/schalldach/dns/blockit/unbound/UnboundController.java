package com.schalldach.dns.blockit.unbound;

import com.schalldach.dns.blockit.unbound.service.UnboundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by @author Thomas Schalldach on 18/12/2019 software@thomas-schalldach.de.
 */
@RestController
@RequestMapping("/unbound")
@Slf4j
public class UnboundController {


    @Autowired
    private UnboundService unboundService;


    @PostMapping("/export")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void exportBlocklist(@RequestBody String configurationName) {
        log.trace("Received new Configuration export request");
        unboundService.exportConfiguration(configurationName);
        log.trace("Finished Configuration export request");
    }

    @PostMapping("/reload")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reloadService() {
        log.trace("Trying to reload service");
        unboundService.reload();
        log.trace("Service reloaded");
    }


}
