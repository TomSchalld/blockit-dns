/*
 * Copyright (c) 2019.
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Thomas Schalldach software@schalldach.com
 */

package com.schalldach.dns.blockit.blocklist.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by @author Thomas Schalldach on 12/12/2019 software@thomas-schalldach.de.
 */
@Service
@Slf4j
public class BlocklistDownloaderImpl implements BlocklistDownloader {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public byte[] fetch(String url) {
        final URI downloadUri = UriComponentsBuilder.fromHttpUrl(url)
                .build(true)
                .toUri();
        log.debug("Starting download of blocklist [{}]", downloadUri);
        final ResponseEntity<byte[]> exchange = restTemplate.exchange(downloadUri, HttpMethod.GET, getHeaders(), byte[].class);
        if (exchange.getStatusCode().isError()) {
            System.err.println("Error");
            return null;
        }
        log.debug("Finished download of blocklist [{}]", downloadUri);

        return exchange.getBody();

    }

    private HttpEntity<?> getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.TEXT_HTML, MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON));
        headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
        headers.set(HttpHeaders.USER_AGENT, "Mozilla");
        return new HttpEntity<>("body", headers);
    }

}
