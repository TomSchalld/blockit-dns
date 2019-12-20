package com.schalldach.dns.blockit.blocklist.service.impl;

import com.schalldach.dns.blockit.blocklist.data.BlacklistRepo;
import com.schalldach.dns.blockit.blocklist.data.BlocklistRegistry;
import com.schalldach.dns.blockit.blocklist.data.BlocklistRepo;
import com.schalldach.dns.blockit.blocklist.data.WhitelistRepo;
import com.schalldach.dns.blockit.blocklist.service.api.BlocklistExporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.schalldach.dns.blockit.blocklist.service.impl.BlocklistServiceImpl.MILLION;
import static com.schalldach.dns.blockit.blocklist.service.impl.BlocklistServiceImpl.THOUSAND;

/**
 * Created by @author Thomas Schalldach on 13/12/2019 software@thomas-schalldach.de.
 */
@Service
@Slf4j
public class BlocklistExporterImpl implements BlocklistExporter {


    @Autowired
    private BlocklistRepo blocklistRepo;
    @Autowired
    private WhitelistRepo whitelistRepo;
    @Autowired
    private BlacklistRepo blacklistRepo;

    @Value("${blocklist.export.format}")
    private SupportedExportFormat exportFormat;

    @Value("${blocklist.export.folder}")
    private Path exportPath;

    @Value("${blocklist.export.filename}")
    private String filename;


    @Override
    @Transactional
    public void exportActive() {
        log.info("Starting blocklist export into format [{}] and into folder [{}]...", exportFormat, exportPath);
        final long startTime = new Date().getTime();
        final File exportFolder = exportPath.toFile();
        if (exportFolder.isDirectory()) {
            final File exportFile = Paths.get(exportPath.toString(), filename).toFile();
            try (OutputStream out = new FileOutputStream(exportFile)) {
                exportFormat.addFileHeader(out);
                final Set<String> blacklisted = new HashSet<>(blacklistRepo.getAllDomains());
                blacklisted.addAll(blocklistRepo.findAllActive()
                        .stream()
                        .flatMap(Collection::stream)
                        .map(BlocklistRegistry::getBlocklist)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet()));
                blacklisted.removeAll(whitelistRepo.getAllDomains());
                blacklisted.stream()
                        .map(exportFormat::map)
                        .forEach(bytes -> {
                            try {
                                out.write(bytes);
                            } catch (IOException e) {
                                log.warn("Could not write Blocklist due to Error", e);
                            }
                        });
                log.info("Finished blocklist export into format [{}], [{}mb] written in [{}]s...", exportFormat, exportFile.length() / MILLION, (new Date().getTime() - startTime) / THOUSAND);
            } catch (IOException e) {
                log.warn("Could not write Blocklist due to Error", e);
            }
        }
    }

    @Transactional
    public void exportActiveNativeQuery() {
        log.info("Starting blocklist export into format [{}] and into folder [{}]...", exportFormat, exportPath);
        final long startTime = new Date().getTime();
        final File exportFolder = exportPath.toFile();
        if (exportFolder.isDirectory()) {
            final File exportFile = Paths.get(exportPath.toString(), filename).toFile();
            try (OutputStream out = new FileOutputStream(exportFile)) {
                exportFormat.addFileHeader(out);
                final Set<String> blocklists = new HashSet<>(blocklistRepo.findAllActiveDomains());
                blocklists.addAll(blacklistRepo.getAllDomains());
                blocklists.removeAll(whitelistRepo.getAllDomains());
                blocklists.stream()
                        .map(exportFormat::map)
                        .forEach(bytes -> {
                            try {
                                out.write(bytes);
                            } catch (IOException e) {
                                log.warn("Could not write Blocklist due to Error", e);
                            }
                        });
                log.info("Finished blocklist export into format [{}], [{}mb] written in [{}]s...", exportFormat, exportFile.length() / MILLION, (new Date().getTime() - startTime) / THOUSAND);
            } catch (IOException e) {
                log.warn("Could not write Blocklist due to Error", e);
            }
        }
    }

}
