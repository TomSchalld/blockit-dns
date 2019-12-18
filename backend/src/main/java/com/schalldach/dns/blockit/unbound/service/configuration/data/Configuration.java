package com.schalldach.dns.blockit.unbound.service.configuration.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(name = "unbound_configuration")
public class Configuration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "configuration_name", unique = true, nullable = false)
    private String configurationName;

    /**
     * The  number  of threads to create to serve clients. Use 1 for no
     * threading.
     */
    @Column(name = "num_threads")
    @FileMapping("num-threads")
    private Integer threadCount;

    /**
     * The port number, default 53, on which  the  server  responds  to
     * queries.
     */
    @Column(name = "port")
    @FileMapping("port")
    private Integer port;

    /**
     * Interface  to  use  to connect to the network. This interface is
     * listened to for queries from clients, and answers to clients are
     * given  from  it.  Can be given multiple times to work on several
     * interfaces. If none are given the default is to listen to local-
     * host.   The  interfaces  are not changed on a reload (kill -HUP)
     * but only on restart.  A port number can be specified with  @port
     * (without spaces between interface and port number), if not spec-
     * ified the default port (from port) is used.
     */
    @Column(name = "interface_ip4")
    @FileMapping("interface")
    private String interfaceIp4;

    /**
     * Interface  to  use  to connect to the network. This interface is
     * listened to for queries from clients, and answers to clients are
     * given  from  it.  Can be given multiple times to work on several
     * interfaces. If none are given the default is to listen to local-
     * host.   The  interfaces  are not changed on a reload (kill -HUP)
     * but only on restart.  A port number can be specified with  @port
     * (without spaces between interface and port number), if not spec-
     * ified the default port (from port) is used.
     */
    @Column(name = "interface_ip6")
    @FileMapping("interface")
    private String interfaceIp6;

    /**
     * Number  of  bytes  size  of  the  message  cache.  Default  is 4
     * megabytes.  A plain number is in bytes, append 'k', 'm'  or  'g'
     * for  kilobytes,  megabytes  or  gigabytes  (1024*1024 bytes in a
     * megabyte).
     */
    @Column(name = "msg_cache_size")
    @FileMapping("msg-cache-size")
    private String messageCacheSize;

    /**
     * Number of slabs in the message cache.  Slabs  reduce  lock  con-
     * tention  by  threads.   Must  be  set  to  a power of 2. Setting
     * (close) to the number of cpus is a reasonable guess.
     */
    @Column(name = "msg_cache_slabs")
    @FileMapping("msg-cache-slabs")
    private Integer messageCacheSlabs;


    /**
     * If not 0, then set the SO_RCVBUF socket option to get more  buf-
     * fer space on UDP port 53 incoming queries.  So that short spikes
     * on busy servers do not drop  packets  (see  counter  in  netstat
     * -su).   Default  is 0 (use system value).  Otherwise, the number
     * of bytes to ask for, try "4m" on a busy server.  The OS caps  it
     * at  a  maximum, on linux unbound needs root permission to bypass
     * the limit, or the admin can use  sysctl  net.core.rmem_max.   On
     * BSD  change kern.ipc.maxsockbuf in /etc/sysctl.conf.  On OpenBSD
     * change header and recompile kernel. On Solaris ndd -set /dev/udp
     * udp_max_buf 8388608.
     */
    @Column(name = "socket_receive_buffer")
    @FileMapping("so-rcvbuf")
    @Default
    private String socketReceiveBuffer = "2m";

    /**
     * If not 0, then set the SO_RCVBUF socket option to get more  buf-
     * fer space on UDP port 53 incoming queries.  So that short spikes
     * on busy servers do not drop  packets  (see  counter  in  netstat
     * -su).   Default  is 0 (use system value).  Otherwise, the number
     * of bytes to ask for, try "4m" on a busy server.  The OS caps  it
     * at  a  maximum, on linux unbound needs root permission to bypass
     * the limit, or the admin can use  sysctl  net.core.rmem_max.   On
     * BSD  change kern.ipc.maxsockbuf in /etc/sysctl.conf.  On OpenBSD
     * change header and recompile kernel. On Solaris ndd -set /dev/udp
     * udp_max_buf 8388608.
     */
    @Column(name = "socket_send_buffer")
    @FileMapping("so-sndbuf")
    @Default
    private String socketSendBuffer = "2m";


    /**
     * Number  of  bytes  size  of  the  message  cache.  Default  is 4
     * megabytes.  A plain number is in bytes, append 'k', 'm'  or  'g'
     * for  kilobytes,  megabytes  or  gigabytes  (1024*1024 bytes in a
     * megabyte).
     */
    @Column(name = "rrset_cache_size")
    @FileMapping("rrset-cache-size")
    private String rrsetCacheSize;

    /**
     * Number of slabs in the message cache.  Slabs  reduce  lock  con-
     * tention  by  threads.   Must  be  set  to  a power of 2. Setting
     * (close) to the number of cpus is a reasonable guess.
     */
    @Column(name = "rrset_cache_slabs")
    @FileMapping("rrset-cache-slabs")
    private Integer rrsetCacheSlabs;

    /**
     * Time  to  live  maximum  for  RRsets  and messages in the cache.
     * Default is 86400 seconds (1 day).  When  the  TTL  expires,  the
     * cache  item has expired.  Can be set lower to force the resolver
     * to query for data often, and not trust (very large) TTL  values.
     * Downstream clients also see the lower TTL.
     */
    @Column(name = "cache_max_ttl")
    @FileMapping("cache-max-ttl")
    private Integer cacheMaxTTL;

    /**
     * Time  to  live  minimum  for  RRsets  and messages in the cache.
     * Default is 0.  If the minimum kicks in, the data is  cached  for
     * longer than the domain owner intended, and thus less queries are
     * made to look up the data.  Zero makes sure the data in the cache
     * is  as the domain owner intended, higher values, especially more
     * than an hour or so, can lead to trouble as the data in the cache
     * does not match up with the actual data any more.
     */
    @Column(name = "cache_min_ttl")
    @FileMapping("cache-min-ttl")
    @Default
    private Integer cacheMinTTL = 3600;

    /**
     * Number  of  slabs in the infrastructure cache. Slabs reduce lock
     * contention by threads. Must be set to a power of 2.
     */
    @Column(name = "infra_cache_slabs")
    @FileMapping("infra-cache-slabs")
    private Integer infrastructureCacheSlabs;

    /**
     * Enable or disable whether ip4 queries are  answered  or  issued.
     * Default is yes.
     */
    @Column(name = "do_ip4")
    @FileMapping("do-ip4")
    @Enumerated(EnumType.STRING)
    @Default
    private UnboundConfBool doIp4 = UnboundConfBool.YES;

    /**
     * Enable  or  disable  whether ip6 queries are answered or issued.
     * Default is yes.  If disabled, queries are not answered on  IPv6,
     * and  queries  are  not sent on IPv6 to the internet nameservers.
     * With this option you can disable the ipv6 transport for  sending
     * DNS traffic, it does not impact the contents of the DNS traffic,
     * which may have ip4 and ip6 addresses in it.
     */
    @Column(name = "do_ip6")
    @FileMapping("do-ip6")
    @Enumerated(EnumType.STRING)
    @Default
    private UnboundConfBool doIp6 = UnboundConfBool.YES;

    /**
     * If enabled, prefer IPv6 transport for  sending  DNS  queries  to
     * internet nameservers. Default is no.
     */
    @Column(name = "prefer_ip6")
    @FileMapping("prefer-ip6")
    @Enumerated(EnumType.STRING)
    @Default
    private UnboundConfBool preferIp6 = UnboundConfBool.NO;


    /**
     * Enable  or  disable  whether UDP queries are answered or issued.
     * Default is yes.
     */
    @Column(name = "do_udp")
    @FileMapping("do-udp")
    @Enumerated(EnumType.STRING)
    @Default
    private UnboundConfBool doUdp = UnboundConfBool.YES;

    /**
     * Enable or disable whether TCP queries are  answered  or  issued.
     * Default is yes.
     */
    @Column(name = "do_tcp")
    @FileMapping("do-tcp")
    @Enumerated(EnumType.STRING)
    @Default
    private UnboundConfBool doTcp = UnboundConfBool.YES;


    /**
     * Enable or disable whether the upstream queries use TCP only  for
     * transport.  Default is no.  Useful in tunneling scenarios.
     */
    @Column(name = "tcp_upstream")
    @FileMapping("tcp-upstream")
    @Enumerated(EnumType.STRING)
    @Default
    private UnboundConfBool tcpUpstream = UnboundConfBool.NO;

    /**
     * Enabled or disable whether the upstream queries use TLS only for
     * transport.   Default is no.  Useful in tunneling scenarios.  The
     * TLS contains plain DNS in TCP wireformat.  The other server must
     * support  this  (see  tls-service-key).  If you enable this, also
     * configure a tls-cert-bundle  or  use  tls-win-cert  to  load  CA
     * certs,  otherwise the connections cannot be authenticated.  This
     * option enables TLS for all of them, but if you do not  set  this
     * you  can  configure TLS specifically for some forward zones with
     * forward-tls-upstream.  And also with stub-tls-upstream.
     */
    @Column(name = "tls_upstream")
    @FileMapping("tls-upstream")
    @Enumerated(EnumType.STRING)
    @Default
    private UnboundConfBool tlsUpstream = UnboundConfBool.NO;


    /**
     * Enabled or disable whether the upstream queries use TLS only for
     * transport.   Default is no.  Useful in tunneling scenarios.  The
     * TLS contains plain DNS in TCP wireformat.  The other server must
     * support  this  (see  tls-service-key).  If you enable this, also
     * configure a tls-cert-bundle  or  use  tls-win-cert  to  load  CA
     * certs,  otherwise the connections cannot be authenticated.  This
     * option enables TLS for all of them, but if you do not  set  this
     * you  can  configure TLS specifically for some forward zones with
     * forward-tls-upstream.  And also with stub-tls-upstream.
     */
    @Column(name = "tls_service_key")
    @FileMapping("tls-service-key")
    private String tlsServiceKey;

    /**
     * control which client ips are allowed to make (recursive) queries to this
     * server. Specify classless netblocks with /size and action.  By default
     * everything is refused, except for localhost.  Choose deny (drop message),
     * refuse (polite error reply), allow (recursive ok), allow_snoop (recursive
     * and nonrecursive ok)
     */
    @Column(name = "access_control")
    @FileMapping("access-control")
    @ElementCollection
    private List<String> accessControl;

    /**
     *
     */
    @Column(name = "root_hints")
    @FileMapping("root-hints")
    private String rootHints;
    /**
     * enable to not answer id.server and hostname.bind queries
     */
    @Column(name = "hide_identity")
    @FileMapping("hide-identity")
    @Enumerated(EnumType.STRING)
    @Default
    private UnboundConfBool hideIdentity = UnboundConfBool.YES;
    /**
     * enable to not answer version.server and version.bind queries.
     */
    @Column(name = "hide_version")
    @FileMapping("hide-version")
    @Enumerated(EnumType.STRING)
    @Default
    private UnboundConfBool hideVersion = UnboundConfBool.YES;

    @Column(name = "hide_trustanchor")
    @FileMapping("hide-trustanchor")
    @Enumerated(EnumType.STRING)
    @Default
    private UnboundConfBool hideTrustanchor = UnboundConfBool.YES;
    /**
     * Will  trust  glue  only  if  it is within the servers authority.
     * Default is on.
     */
    @Column(name = "harden_glue")
    @FileMapping("harden-glue")
    @Enumerated(EnumType.STRING)
    @Default
    private UnboundConfBool hardenGlue = UnboundConfBool.YES;

    /**
     * Will  trust  glue  only  if  it is within the servers authority.
     * Default is on.
     */
    @Column(name = "harden_dnssec_stripped")
    @FileMapping("harden-dnssec-stripped")
    @Enumerated(EnumType.STRING)
    @Default
    private UnboundConfBool hardenDnsSecStripped = UnboundConfBool.YES;

    /**
     * Use  0x20-encoded  random  bits  in  the  query  to  foil  spoof
     * attempts.  This perturbs the lowercase and  uppercase  of  query
     * names  sent  to  authority servers and checks if the reply still
     * has the correct casing.  Disabled by default.  This  feature  is
     * an experimental implementation of draft dns-0x20.
     */
    @Column(name = "use_caps_for_id")
    @FileMapping("use-caps-for-id")
    @Enumerated(EnumType.STRING)
    @Default
    private UnboundConfBool useCapsForId = UnboundConfBool.YES;

    /**
     * Give IPv4 of IPv6 addresses  or  classless  subnets.  These  are
     * addresses  on  your  private  network, and are not allowed to be
     * returned for public internet  names.   Any  occurrence  of  such
     * addresses are removed from DNS answers. Additionally, the DNSSEC
     * validator may mark the  answers  bogus.  This  protects  against
     * so-called  DNS  Rebinding, where a user browser is turned into a
     * network proxy, allowing remote access  through  the  browser  to
     * other  parts of your private network.  Some names can be allowed
     * to contain your private addresses, by default all the local-data
     * that  you  configured  is  allowed to, and you can specify addi-
     * tional names using private-domain.   No  private  addresses  are
     * enabled  by default.  We consider to enable this for the RFC1918
     * private IP address space by  default  in  later  releases.  That
     * would  enable  private  addresses  for  10.0.0.0/8 172.16.0.0/12
     * 192.168.0.0/16 169.254.0.0/16 fd00::/8 and fe80::/10, since  the
     * RFC  standards  say these addresses should not be visible on the
     * public internet.  Turning on 127.0.0.0/8 would hinder many spam-
     * blocklists   as  they  use  that.   Adding  ::ffff:0:0/96  stops
     * IPv4-mapped IPv6 addresses from bypassing the filter.
     */
    @Column(name = "private_address")
    @FileMapping("private-address")
    @ElementCollection
    private List<String> privateAddress;

    /**
     * Allow this domain, and all its  subdomains  to  contain  private
     * addresses.   Give  multiple times to allow multiple domain names
     * to contain private addresses. Default is none.
     */
    @Column(name = "private_domain")
    @FileMapping("private-domain")
    @ElementCollection
    private List<String> privateDomain;

    /**
     * If set, a total number of unwanted replies is kept track  of  in
     * every thread.  When it reaches the threshold, a defensive action
     * is taken and a warning is printed to  the  log.   The  defensive
     * action  is  to  clear  the  rrset  and message caches, hopefully
     * flushing away any poison.  A value of 10 million  is  suggested.
     * Default is 0 (turned off).
     */
    @Column(name = "unwanted_reply_threshold")
    @FileMapping("unwanted-reply-threshold")
    @Default
    private Integer unwantedReplyThreshold = 10000;

    /**
     * If  yes, localhost is added to the do-not-query-address entries,
     * both IP6 ::1 and IP4 127.0.0.1/8. If no, then localhost  can  be
     * used to send queries to. Default is yes.
     */
    @Column(name = "do_not_query_localhost")
    @FileMapping("do-not-query-localhost")
    @Enumerated(EnumType.STRING)
    private UnboundConfBool doNotQueryLocalhost = UnboundConfBool.YES;

    /**
     * If yes, message cache elements are prefetched before they expire
     * to keep the cache up to date.  Default is  no.   Turning  it  on
     * gives about 10 percent more traffic and load on the machine, but
     * popular items do not expire from the cache.
     */
    @Column(name = "prefetch")
    @FileMapping("prefetch")
    @Enumerated(EnumType.STRING)
    private UnboundConfBool prefetch = UnboundConfBool.YES;


    /**
     * Instruct  the  validator to remove data from the additional sec-
     * tion of secure messages that are not signed  properly.  Messages
     * that  are  insecure,  bogus,  indeterminate or unchecked are not
     * affected. Default is yes. Use this setting to protect the  users
     * that  rely on this validator for authentication from potentially
     * bad data in the additional section.
     */
    @Column(name = "val_clean_additional")
    @FileMapping("val-clean-additional")
    @Enumerated(EnumType.STRING)
    private UnboundConfBool valCleanAdditional = UnboundConfBool.YES;


    /**
     * Number  of  bytes size of the key cache. Default is 4 megabytes.
     * A plain number is in bytes, append 'k', 'm'  or  'g'  for  kilo-
     * bytes, megabytes or gigabytes (1024*1024 bytes in a megabyte).
     */
    @Column(name = "key_cache_size")
    @FileMapping("key-cache-size")
    private String keyCacheSize;

    /**
     * Number  of  slabs in the key cache. Slabs reduce lock contention
     * by threads.  Must be set to a power of 2. Setting (close) to the
     * number of cpus is a reasonable guess.
     */
    @Column(name = "key_cache_slabs")
    @FileMapping("key-cache-slabs")
    private Integer keyCacheSlabs;

    @Column(name = "forward_addr")
    @FileMapping("forward-addr")
    @ElementCollection
    private List<String> forwardAddress;

}
/*


       local-zone: <zone> <type>
              Configure  a  local zone. The type determines the answer to give
              if there is no  match  from  local-data.  The  types  are  deny,
              refuse,  static, transparent, redirect, nodefault, typetranspar-
              ent, inform, inform_deny,  inform_redirect,  always_transparent,
              always_refuse, always_nxdomain, noview, and are explained below.
              After that the default settings are listed. Use  local-data:  to
              enter  data  into  the  local  zone. Answers for local zones are
              authoritative DNS answers. By default the zones are class IN.

              If you need more complicated authoritative data, with referrals,
              wildcards, CNAME/DNAME support, or DNSSEC authoritative service,
              setup a stub-zone for it as detailed in the  stub  zone  section
              below.

            deny Do  not  send an answer, drop the query.  If there is a match
                 from local data, the query is answered.

            refuse
                 Send an error message reply, with rcode REFUSED.  If there is
                 a match from local data, the query is answered.

            static
                 If  there  is a match from local data, the query is answered.
                 Otherwise, the query is answered  with  nodata  or  nxdomain.
                 For  a  negative  answer  a  SOA is included in the answer if
                 present as local-data for the zone apex domain.

            transparent
                 If there is a match from local data, the query  is  answered.
                 Otherwise  if  the  query  has a different name, the query is
                 resolved normally.  If the query  is  for  a  name  given  in
                 localdata  but  no  such  type of data is given in localdata,
                 then a noerror nodata answer is returned.  If  no  local-zone
                 is  given  local-data causes a transparent zone to be created
                 by default.

            typetransparent
                 If there is a match from local data, the query  is  answered.
                 If  the  query  is for a different name, or for the same name
                 but for a different type, the  query  is  resolved  normally.
                 So,  similar  to transparent but types that are not listed in
                 local data are resolved normally, so if an A record is in the
                 local  data  that  does  not  cause  a  nodata reply for AAAA
                 queries.

            redirect
                 The query is answered from the local data for the zone  name.
                 There  may  be  no  local  data  beneath the zone name.  This
                 answers queries for the zone, and all subdomains of the  zone
                 with the local data for the zone.  It can be used to redirect
                 a domain to return a different  address  record  to  the  end
                 user,    with   local-zone:   "example.com."   redirect   and
                 local-data: "example.com. A 127.0.0.1" queries for  www.exam-
                 ple.com and www.foo.example.com are redirected, so that users
                 with web browsers  cannot  access  sites  with  suffix  exam-
                 ple.com.

            inform
                 The  query  is  answered  normally, same as transparent.  The
                 client IP address (@portnumber) is printed  to  the  logfile.
                 The  log  message  is: timestamp, unbound-pid, info: zonename
                 inform IP@port queryname type class.  This option can be used
                 for normal resolution, but machines looking up infected names
                 are logged, eg. to run antivirus on them.

            inform_deny
                 The query is dropped, like 'deny', and logged, like 'inform'.
                 Ie. find infected machines without answering the queries.

            inform_redirect
                 The  query  is  redirected, like 'redirect', and logged, like
                 'inform'.  Ie. answer queries with fixed data  and  also  log
                 the machines that ask.

            always_transparent
                 Like  transparent,  but  ignores local data and resolves nor-
                 mally.

            always_refuse
                 Like refuse, but ignores local data and refuses the query.

            always_nxdomain
                 Like static, but ignores local data and returns nxdomain  for
                 the query.

            noview
                 Breaks  out  of  that view and moves towards the global local
                 zones for answer to the query.  If  the  view  first  is  no,
                 it'll  resolve  normally.   If  view  first is enabled, it'll
                 break perform that step and check the  global  answers.   For
                 when  the  view has view specific overrides but some zone has
                 to be answered from global local zone contents.

            nodefault
                 Used to turn off default contents for AS112 zones. The  other
                 types also turn off default contents for the zone. The 'node-
                 fault' option has no other effect than  turning  off  default
                 contents  for  the  given  zone.   Use  nodefault  if you use
                 exactly that zone, if you want to use a subzone,  use  trans-
                 parent.

       The  default zones are localhost, reverse 127.0.0.1 and ::1, the onion,
       test, invalid and the AS112 zones. The  AS112  zones  are  reverse  DNS
       zones  for  private use and reserved IP addresses for which the servers
       on the internet cannot provide correct answers. They are configured  by
       default to give nxdomain (no reverse information) answers. The defaults
       can be turned off by specifying your own local-zone of  that  name,  or
       using  the  'nodefault'  type. Below is a list of the default zone con-
       tents.

            localhost
                 The IP4 and IP6 localhost information is given.  NS  and  SOA
                 records are provided for completeness and to satisfy some DNS
                 update tools. Default content:
                 local-zone: "localhost." redirect
                 local-data: "localhost. 10800 IN NS localhost."
                 local-data: "localhost. 10800 IN
                     SOA localhost. nobody.invalid. 1 3600 1200 604800 10800"
                 local-data: "localhost. 10800 IN A 127.0.0.1"
                 local-data: "localhost. 10800 IN AAAA ::1"

            reverse IPv4 loopback
                 Default content:
                 local-zone: "127.in-addr.arpa." static
                 local-data: "127.in-addr.arpa. 10800 IN NS localhost."
                 local-data: "127.in-addr.arpa. 10800 IN
                     SOA localhost. nobody.invalid. 1 3600 1200 604800 10800"
                 local-data: "1.0.0.127.in-addr.arpa. 10800 IN
                     PTR localhost."

            reverse IPv6 loopback
                 Default content:
                 local-zone: "1.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.
                     0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.ip6.arpa." static
                 local-data: "1.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.
                     0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.ip6.arpa. 10800 IN
                     NS localhost."
                 local-data: "1.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.
                     0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.ip6.arpa. 10800 IN
                     SOA localhost. nobody.invalid. 1 3600 1200 604800 10800"
                 local-data: "1.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.
                     0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.ip6.arpa. 10800 IN
                     PTR localhost."

            onion (RFC 7686)
                 Default content:
                 local-zone: "onion." static
                 local-data: "onion. 10800 IN NS localhost."
                 local-data: "onion. 10800 IN
                     SOA localhost. nobody.invalid. 1 3600 1200 604800 10800"

            test (RFC 2606)
                 Default content:
                 local-zone: "test." static
                 local-data: "test. 10800 IN NS localhost."
                 local-data: "test. 10800 IN
                     SOA localhost. nobody.invalid. 1 3600 1200 604800 10800"

            invalid (RFC 2606)
                 Default content:
                 local-zone: "invalid." static
                 local-data: "invalid. 10800 IN NS localhost."
                 local-data: "invalid. 10800 IN
                     SOA localhost. nobody.invalid. 1 3600 1200 604800 10800"

            reverse RFC1918 local use zones
                 Reverse data for zones  10.in-addr.arpa,  16.172.in-addr.arpa
                 to     31.172.in-addr.arpa,     168.192.in-addr.arpa.     The
                 local-zone: is set static  and  as  local-data:  SOA  and  NS
                 records are provided.

            reverse RFC3330 IP4 this, link-local, testnet and broadcast
                 Reverse  data for zones 0.in-addr.arpa, 254.169.in-addr.arpa,
                 2.0.192.in-addr.arpa (TEST  NET  1),  100.51.198.in-addr.arpa
                 (TEST   NET   2),   113.0.203.in-addr.arpa   (TEST   NET  3),
                 255.255.255.255.in-addr.arpa.  And  from  64.100.in-addr.arpa
                 to 127.100.in-addr.arpa (Shared Address Space).

            reverse RFC4291 IP6 unspecified
                 Reverse data for zone
                 0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.
                 0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.ip6.arpa.

            reverse RFC4193 IPv6 Locally Assigned Local Addresses
                 Reverse data for zone D.F.ip6.arpa.

            reverse RFC4291 IPv6 Link Local Addresses
                 Reverse data for zones 8.E.F.ip6.arpa to B.E.F.ip6.arpa.

            reverse IPv6 Example Prefix
                 Reverse  data for zone 8.B.D.0.1.0.0.2.ip6.arpa. This zone is
                 used for tutorials and examples. You can remove the block  on
                 this zone with:
                   local-zone: 8.B.D.0.1.0.0.2.ip6.arpa. nodefault
                 You can also selectively unblock a part of the zone by making
                 that part transparent with a local-zone statement.  This also
                 works with the other default zones.

       local-data: "<resource record string>"
            Configure  local data, which is served in reply to queries for it.
            The query has to match exactly unless you configure the local-zone
            as  redirect.  If  not matched exactly, the local-zone type deter-
            mines further processing. If local-data is configured that is  not
            a  subdomain  of a local-zone, a transparent local-zone is config-
            ured.  For record types such as TXT,  use  single  quotes,  as  in
            local-data: 'example. TXT "text"'.

            If  you  need more complicated authoritative data, with referrals,
            wildcards, CNAME/DNAME support, or DNSSEC  authoritative  service,
            setup  a  stub-zone  for  it  as detailed in the stub zone section
            below.

       local-data-ptr: "IPaddr name"
            Configure local data shorthand for a PTR record with the  reversed
            IPv4  or  IPv6  address and the host name.  For example "192.0.2.4
            www.example.com".  TTL can be  inserted  like  this:  "2001:DB8::4
            7200 www.example.com"

       local-zone-tag: <zone> <"list of tags">
            Assign  tags to localzones. Tagged localzones will only be applied
            when the used access-control element has a matching tag. Tags must
            be  defined  in  define-tags.  Enclose list of tags in quotes ("")
            and put spaces between tags.  When  there  are  multiple  tags  it
            checks  if  the intersection of the list of tags for the query and
            local-zone-tag is non-empty.

       local-zone-override: <zone> <IP netblock> <type>
            Override the localzone type for queries  from  addresses  matching
            netblock.  Use this localzone type, regardless the type configured
            for the local-zone (both tagged and untagged) and  regardless  the
            type configured using access-control-tag-action.
*/