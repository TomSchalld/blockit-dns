package com.schalldach.dns.blockit.logging;

import com.schalldach.dns.blockit.logging.service.LogAware.Log;
import com.schalldach.dns.blockit.logging.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by @author Thomas Schalldach on 01/01/2020 software@thomas-schalldach.de.
 */
@RestController
@RequestMapping("/logging")
public class LogController {


    @Autowired
    private LogService logService;

    @GetMapping
    public ResponseEntity<Log> getLogs() {
        return new ResponseEntity<>(logService.getLog(), HttpStatus.OK);
    }

    @GetMapping("/replies")
    public ResponseEntity<Log> getReplyLogs() {
        return new ResponseEntity<>(logService.getRepliesLog(), HttpStatus.OK);
    }

    @GetMapping("/refused")
    public ResponseEntity<Log> getRefusedLogs() {
        return new ResponseEntity<>(logService.getRefusedLog(), HttpStatus.OK);
    }
}
