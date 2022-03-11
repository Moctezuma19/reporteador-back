package com.jrivera.reporteador.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RespuestaDto {
    private Integer idIncidencia;
    private Integer idUsuario;
    private String descripcion;
    private Integer estado;
}
