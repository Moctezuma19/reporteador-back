package com.jrivera.reporteador.servicio.impl;

import com.jrivera.reporteador.modelo.Incidencia;
import com.jrivera.reporteador.repositorio.IncidenciaRepositorio;
import com.jrivera.reporteador.servicio.ImagenServicio;
import com.jrivera.reporteador.util.Codificador;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.IOUtils;

import java.io.*;
import java.util.Base64;

@Service
public class ImagenServicioImpl implements ImagenServicio {

    static final Logger LOG = LoggerFactory.getLogger(ImagenServicioImpl.class);

    @Value("${reporteador.imagenes}")
    private String ImagenesUrl;

    @Autowired
    IncidenciaRepositorio incidenciaRepositorio;

    @Override
    public String guardaImagen(Integer idIncidencia, MultipartFile imagen) {
        Incidencia incidencia = incidenciaRepositorio.findById(idIncidencia).orElse(null);
        if (incidencia == null) {
            return null;
        }
        String hash = Codificador.Hash(idIncidencia, imagen.getName());
        try {
            byte[] a = IOUtils.readFully(imagen.getInputStream(), -1, true);
            byte[] b64 = Base64.getEncoder().encode(a);
            FileOutputStream fileOutputStream = new FileOutputStream(ImagenesUrl + "/" + hash);
            fileOutputStream.write(b64);
            return hash;
        } catch (IOException ie) {
            LOG.error(ie.getMessage());
            return null;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return null;
        }
    }

    @Override
    public byte[] obtenImagen(String hash) {
        try {
            File f = new File(ImagenesUrl + "/" + hash);
            if (!f.exists()) {
                return null;
            }
            LOG.info("Obteniendo: " + ImagenesUrl + "/" + hash);
            InputStream in = new FileInputStream(f);
            return IOUtils.readFully(in, -1, true);
        } catch (IOException ie) {
            return null;
        }
    }

}
