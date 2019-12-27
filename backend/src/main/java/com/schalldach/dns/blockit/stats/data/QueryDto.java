package com.schalldach.dns.blockit.stats.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

/**
 * Created by @author Thomas Schalldach on 26/12/2019 software@thomas-schalldach.de.
 */
@AllArgsConstructor
@Getter
public class QueryDto {
    private Date point;
    private Long queries;
}
