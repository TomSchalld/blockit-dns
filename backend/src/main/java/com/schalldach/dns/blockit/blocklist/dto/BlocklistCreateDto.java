/*
 * Copyright (c) 2019.
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Thomas Schalldach software@schalldach.com
 */

package com.schalldach.dns.blockit.blocklist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by @author Thomas Schalldach on 12/12/2019 software@thomas-schalldach.de.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BlocklistCreateDto {
    private String url;
    private boolean active;
}
