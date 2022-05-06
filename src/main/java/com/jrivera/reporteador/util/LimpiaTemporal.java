package com.jrivera.reporteador.util;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.io.IOException;

@Configuration
@EnableScheduling
public class LimpiaTemporal {

    static final Logger LOG = LoggerFactory.getLogger(LimpiaTemporal.class);

    @Value("${reporteador.temp}")
    private String carpetaTemporal;

    @Scheduled(cron = "0 0 0 * * ?")
    public void limpiaDocumentosTemporales() {
        try {
            FileUtils.cleanDirectory(new File(carpetaTemporal));
            LOG.info("Se eliminaro los archivos temporales");
        } catch (IOException io) {
            LOG.error("Hubo un error al tratar de eliminar los archivos");
        }

    }
}
