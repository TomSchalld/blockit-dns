package com.schalldach.dns.blockit.blocklist.data;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by @author Thomas Schalldach on 19/12/2019 software@thomas-schalldach.de.
 */
@Entity
@Data
@Table(name = "custom_whitelisted_domain")
public class CustomWhitelistedDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column(unique = true)
    private String domain;

}
