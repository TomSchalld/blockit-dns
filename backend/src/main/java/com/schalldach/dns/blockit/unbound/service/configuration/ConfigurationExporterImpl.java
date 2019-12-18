package com.schalldach.dns.blockit.unbound.service.configuration;

import com.schalldach.dns.blockit.unbound.service.configuration.data.Configuration;
import com.schalldach.dns.blockit.unbound.service.configuration.util.ConfigurationParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import static com.schalldach.dns.blockit.blocklist.service.impl.BlocklistServiceImpl.THOUSAND;

/**
 * Created by @author Thomas Schalldach on 18/12/2019 software@thomas-schalldach.de.
 */
@Slf4j
public class ConfigurationExporterImpl implements ConfigurationExporter {

    public static final String SERVER = "server:" + System.lineSeparator();

    @Value("${configuration.export.folder}")
    private Path exportPath;

    @Value("${configuration.export.file.postfix}")
    private String filePostFix;


    @Override
    public void export(Configuration config) {
        final String filename = config.getConfigurationName() + filePostFix;
        log.info("Starting [{}] configuration export into folder [{}]...", filename, exportPath);
        final long startTime = new Date().getTime();
        final File exportFolder = exportPath.toFile();
        if (exportFolder.isDirectory()) {
            final File exportFile = Paths.get(exportPath.toString(), filename).toFile();
            try (OutputStream out = new FileOutputStream(exportFile)) {
                out.write(SERVER.getBytes(StandardCharsets.UTF_8));
                List<byte[]> configData = ConfigurationParser.getBytes(config);
                for (byte[] configDatum : configData) {
                    out.write(configDatum);
                }
                log.info("Finished configuration export of [{}], [{}kb] written in [{}]s...", filename, exportFile.length() / THOUSAND, (new Date().getTime() - startTime) / THOUSAND);
            } catch (IOException e) {
                log.warn("Could not write configuration due to Error", e);
            }
        }
    }
}
