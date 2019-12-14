package com.schalldach.dns.blockit.blocklist.service.impl;

import com.schalldach.dns.blockit.blocklist.data.BlocklistRegistry;
import com.schalldach.dns.blockit.blocklist.data.BlocklistRepo;
import com.schalldach.dns.blockit.blocklist.service.api.BlocklistExporter;
import com.schalldach.dns.blockit.blocklist.service.api.BlocklistParser;
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
import java.util.List;
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
    private BlocklistParser blocklistParser;

    @Autowired
    private BlocklistRepo blocklistRepo;

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
                final List<byte[]> collection = blocklistRepo.findAll()
                        .stream().filter(BlocklistRegistry::isActive)
                        .map(blocklistRegistry -> blocklistParser.parse(blocklistRegistry.getData().getEntry()))
                        .parallel().flatMap(Collection::stream)
                        .collect(Collectors.toSet())
                        .stream()
                        .parallel().map(exportFormat::map)
                        .collect(Collectors.toUnmodifiableList());
                for (byte[] bytes : collection) {
                    out.write(bytes);
                }
                log.info("Finished blocklist export into format [{}], [{}mb] written in [{}]s...", exportFormat, exportFile.length() / MILLION, (new Date().getTime() - startTime) / THOUSAND);
            } catch (IOException e) {
                log.warn("Could not write Blocklist due to Error", e);
            }
        }
    }

}
