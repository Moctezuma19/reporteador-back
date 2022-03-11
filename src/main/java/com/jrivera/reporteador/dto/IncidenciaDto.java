package com.jrivera.reporteador.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class IncidenciaDto {
    private Integer idUsuario;
    private String titulo;
    private String descripcion;
    private String imagen1Hash;
    private String imagen2Hash;
    private MultipartFile imagen1;
    private MultipartFile imagen2;
}
