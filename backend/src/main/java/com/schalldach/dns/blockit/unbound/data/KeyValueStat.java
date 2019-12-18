package com.schalldach.dns.blockit.unbound.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by @author Thomas Schalldach on 17/12/2019 software@thomas-schalldach.de.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "unbound_stats_point")
public class KeyValueStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "unbound_stats_data_id")
    private DataPoint dataPoint;
    @Column(name = "stats_key")
    private String key;
    @Column(name = "stats_value")
    private String value;

}
