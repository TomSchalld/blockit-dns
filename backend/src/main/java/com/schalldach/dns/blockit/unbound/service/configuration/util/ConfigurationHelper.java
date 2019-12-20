package com.schalldach.dns.blockit.unbound.service.configuration.util;

import com.schalldach.dns.blockit.unbound.service.configuration.UnboundConfigurerImpl;
import com.schalldach.dns.blockit.unbound.service.configuration.data.Configuration;
import com.schalldach.dns.blockit.unbound.service.configuration.data.UnboundConfBool;

import java.util.Collections;
import java.util.List;

/**
 * Created by @author Thomas Schalldach on 18/12/2019 software@thomas-schalldach.de.
 */
public final class ConfigurationHelper {

    private ConfigurationHelper() {
    }

    public static Configuration getDefaultConfig() {
        return Configuration.builder()
                .configurationName(UnboundConfigurerImpl.DEFAULT)
                .port(53)
                .interfaceIp4("0.0.0.0")
                .doIp4(UnboundConfBool.YES)
                .doIp6(UnboundConfBool.NO)
                .doUdp(UnboundConfBool.YES)
                .doTcp(UnboundConfBool.YES)
                .accessControl(List.of("10.0.0.0/8 allow", "127.0.0.0/8 allow", "192.168.0.0/16 allow"))
                .hideIdentity(UnboundConfBool.YES)
                .hideVersion(UnboundConfBool.YES)
                .hardenGlue(UnboundConfBool.YES)
                .hardenDnsSecStripped(UnboundConfBool.YES)
                .useCapsForId(UnboundConfBool.YES)
                .cacheMinTTL(3600)
                .cacheMaxTTL(86400)
                .prefetch(UnboundConfBool.YES)
                .threadCount(4)
                .messageCacheSlabs(8)
                .rrsetCacheSlabs(8)
                .infrastructureCacheSlabs(8)
                .keyCacheSlabs(8)
                .rrsetCacheSize("256m")
                .messageCacheSize("128m")
                .socketReceiveBuffer("2m")
                .privateAddress(List.of("192.168.0.0/16", "172.16.0.0/12", "10.0.0.0/8"))
                .privateDomain(Collections.singletonList("home.lan"))
                .unwantedReplyThreshold(100000)
                .doNotQueryLocalhost(UnboundConfBool.YES)
                .valCleanAdditional(UnboundConfBool.YES)
                .forwardAddress(List.of("1.1.1.1@53 # Cloudflare",
                        "8.8.8.8@53 # GoogleDNS",
                        "208.67.222.220 # OpenDNS",
                        "1.0.0.1 # Cloudflare",
                        "8.8.4.4 # GoogleDNS",
                        "208.67.222.222 # OpenDNS"))
                .build();
    }

}
