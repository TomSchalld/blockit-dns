package com.schalldach.dns.blockit.blocklist.service;

import com.schalldach.dns.blockit.blocklist.data.BlocklistData;
import com.schalldach.dns.blockit.blocklist.dto.BlocklistCreateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by @author Thomas Schalldach on 13/12/2019 software@thomas-schalldach.de.
 */
@Service
@Slf4j
@Transactional
public class BlocklistPersisterImpl implements BlocklistPersister {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;


    @Override
    public void persist(BlocklistCreateDto dto, Collection<BlocklistData> blocklistEntries) {
        final Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("active", dto.isActive());
        paramMap.put("url", dto.getUrl());
        paramMap.put("version", 0);
        jdbcTemplate.update("insert into blocklist_registry(active, url, version) VALUES (:active,:url,:version)", paramMap);
        final List<Long> idList = jdbcTemplate.query("select id from blocklist_registry where url=:url", paramMap, (rs, rowNum) -> rs.getLong(1));
        final Long registrationId = idList.get(0);
        log.info("Accuired registration Id [{}]", registrationId);
        final List<Map<String, Object>> listParams = blocklistEntries.stream().map(data -> {
            final Map<String, Object> map = new HashMap<>();
            map.put("entry", data.getEntry());
            map.put("id", registrationId);
            return map;
        }).collect(Collectors.toUnmodifiableList());
        final int[] updatedRows = jdbcTemplate.batchUpdate("insert into blocklist_data(entry,blocklist_registry_id) VALUES (:entry,:id)", listParams.toArray(new HashMap[0]));
        log.info("Inserted [{}] new entries", blocklistEntries.size());

    }

}
