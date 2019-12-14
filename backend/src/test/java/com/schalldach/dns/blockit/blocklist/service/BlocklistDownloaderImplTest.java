/*
 * Copyright (c) 2019.
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Thomas Schalldach software@schalldach.com
 */

package com.schalldach.dns.blockit.blocklist.service;

import com.schalldach.dns.blockit.blocklist.service.api.BlocklistDownloader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.stream.Stream;

/**
 * Created by @author Thomas Schalldach on 12/12/2019 software@thomas-schalldach.de.
 */
@SpringBootTest
@Slf4j
public class BlocklistDownloaderImplTest {
    private final static String SERVICE_URL = "http://localhost:8080/blocklists";
    final URI serviceUri = UriComponentsBuilder.fromHttpUrl(SERVICE_URL).build(true).toUri();


    private final static String[] URLS = {"http://pgl.yoyo.org/adservers/serverlist.php?hostformat=none&showintro=0&mimetype=plaintext",
            "https://blocklist.site/app/dl/ads",
            "https://blocklist.site/app/dl/crypto",
            "https://blocklist.site/app/dl/drugs",
            "https://blocklist.site/app/dl/fraud",
            "https://blocklist.site/app/dl/fakenews",
            "https://blocklist.site/app/dl/gambling",
            "https://blocklist.site/app/dl/malware",
            "https://blocklist.site/app/dl/phishing",
            "https://blocklist.site/app/dl/piracy",
            "https://blocklist.site/app/dl/proxy",
            "https://blocklist.site/app/dl/ransomware",
            "https://blocklist.site/app/dl/redirect",
            "https://blocklist.site/app/dl/scam",
            "https://blocklist.site/app/dl/spam",
            "https://blocklist.site/app/dl/torrent",
            "https://blocklist.site/app/dl/tracking",
            "https://blocklist.site/app/dl/facebook",
            "https://blocklist.site/app/dl/youtube"};

    private final static String[] URLS2 = {"http://pgl.yoyo.org/adservers/serverlist.php?hostformat=none&showintro=0&mimetype=plaintext"};

    @Autowired
    private BlocklistDownloader blocklistDownloader;

    @Test
    @Ignore
    public void fetch() {
//        final String url = "https://blocklist.site/app/dl/ads";
        final String url = "http://pgl.yoyo.org/adservers/serverlist.php?hostformat=none&showintro=0&mimetype=plaintext";
        final byte[] fetch = blocklistDownloader.fetch(url);
        Assert.notNull(fetch, "Fetched data null");

    }

    @Test
    @Ignore
    public void prepopulte() {
        final RestTemplate template = new RestTemplate();
        Stream.of(URLS).forEach(url -> {
            final HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth("user", "user");
            httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
            ResponseEntity<String> response = template.exchange(serviceUri, HttpMethod.POST, new HttpEntity<>(new RequestDto(url, true), httpHeaders), String.class);
        });

    }

    @Test
    @Ignore
    public void prepopulteone() {
        final RestTemplate template = new RestTemplate();
        Stream.of(URLS2).forEach(url -> {
            final HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth("user", "user");
            httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
            ResponseEntity<String> response = template.exchange(serviceUri, HttpMethod.POST, new HttpEntity<>(new RequestDto(url, true), httpHeaders), String.class);
        });

    }

    @AllArgsConstructor
    @Getter
    public static class RequestDto {
        private String url;
        private boolean active;
    }

}