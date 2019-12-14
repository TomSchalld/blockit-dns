/*
 * Copyright (c) 2019.
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Thomas Schalldach software@schalldach.com
 */

package com.schalldach.dns.blockit.blocklist.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by @author Thomas Schalldach on 12/12/2019 software@thomas-schalldach.de.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "blocklist_data")
public class BlocklistData {

    @Id
    @Column
    private Long id;

    @Column(name = "blocklist_registry_id")
    private Long blocklistRegistryId;


    @Column
    private String entry;

    public BlocklistData(final String entry) {
        this.entry = entry;
    }
}
