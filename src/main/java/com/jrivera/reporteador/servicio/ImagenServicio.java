package com.jrivera.reporteador.servicio;

import org.springframework.web.multipart.MultipartFile;

public interface ImagenServicio {

    String guardaImagen(Integer idIncidencia, MultipartFile imagen);

    byte[] obtenImagen(String hash);
}
