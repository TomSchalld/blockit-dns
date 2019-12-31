package com.schalldach.dns.blockit.stats.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "unbound_daily_stats_point")
public class HourlyKeyValueStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "unbound_stats_data_id")
    private HourlyDataPoint dataPoint;
    @Column(name = "stats_key")
    private String key;
    @Column(name = "stats_value")
    private String value;

}
