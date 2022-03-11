package com.jrivera.reporteador.controlador;

import com.jrivera.reporteador.servicio.ImagenServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/imagen")
public class ImagenControlador {

    @Autowired
    ImagenServicio imagenServicio;

    @GetMapping(value = "/obten/{hash}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] obtenImagen(@PathVariable String hash) {
        return imagenServicio.obtenImagen(hash);
    }
}
