/*
 * Copyright (c) 2019.
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Thomas Schalldach software@schalldach.com
 */

package com.schalldach.dns.blockit.blocklist.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

/**
 * Created by @author Thomas Schalldach on 12/12/2019 software@thomas-schalldach.de.
 */
@SpringBootTest
@Slf4j
class BlocklistDownloaderImplTest {

    @Autowired
    private BlocklistDownloader blocklistDownloader;

    @Test
    void fetch() {
//        final String url = "https://blocklist.site/app/dl/ads";
        final String url = "http://pgl.yoyo.org/adservers/serverlist.php?hostformat=none&showintro=0&mimetype=plaintext";
        final byte[] fetch = blocklistDownloader.fetch(url);
        Assert.notNull(fetch, "Fetched data null");

    }

}