/*
 * Copyright (c) 2019.
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Thomas Schalldach software@schalldach.com
 */

package com.schalldach.dns.blockit.blocklist;

import com.schalldach.dns.blockit.blocklist.data.BlocklistRegistry;
import com.schalldach.dns.blockit.blocklist.dto.BlocklistCreateDto;
import com.schalldach.dns.blockit.blocklist.dto.BlocklistResponseDto;
import com.schalldach.dns.blockit.blocklist.service.BlocklistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by @author Thomas Schalldach on 12/12/2019 software@thomas-schalldach.de.
 */
@RestController
@RequestMapping("/blocklists")
@Slf4j
public class BlocklistController {

    @Autowired
    private BlocklistService blocklistService;

    @GetMapping
    public ResponseEntity<List<BlocklistResponseDto>> getAll() {
        return new ResponseEntity<>(mapToDto(blocklistService.findAll()), HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addBlocklist(@RequestBody BlocklistCreateDto dto) {
        log.trace("Received new Blocklist creation request");
        blocklistService.create(dto);
        log.trace("Finished Blocklist creation request");
    }

    @PostMapping("/export")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void exportBlocklist() {
        log.trace("Received new Blocklist creation request");
        blocklistService.export();
        log.trace("Finished Blocklist creation request");
    }

    @DeleteMapping("/{url}")
    public ResponseEntity<Void> delete(@PathVariable String url) {
        blocklistService.deleteByUrl(url);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private List<BlocklistResponseDto> mapToDto(final List<BlocklistRegistry> blocklistRegistries) {
        return blocklistRegistries.stream().map(blocklistRegistry -> new BlocklistResponseDto(blocklistRegistry.getUrl(), blocklistRegistry.isActive())).collect(Collectors.toList());
    }

    private BlocklistResponseDto mapToDto(final BlocklistRegistry blocklistRegistry) {
        return new BlocklistResponseDto(blocklistRegistry.getUrl(), blocklistRegistry.isActive());
    }


}
