/*
 * Copyright (c) 2019.
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Thomas Schalldach software@schalldach.com
 */

package com.schalldach.dns.blockit.blocklist.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import java.util.Set;

/**
 * Created by @author Thomas Schalldach on 12/12/2019 software@thomas-schalldach.de.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "blocklist_registry", uniqueConstraints = @UniqueConstraint(columnNames = "url"))
public class BlocklistRegistry {

    @Id
    @Column
    private Long id;
    @Column
    private String url;
    @Column
    private boolean active;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "blocklist_registry_id")
    private Set<BlocklistData> dataSet;

    @Version
    @Column
    private Long version;


}
