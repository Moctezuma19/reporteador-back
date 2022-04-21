package com.jrivera.reporteador.dto;

import lombok.Data;

import java.util.List;

@Data
public class FiltroIncidenciaDto {
    private Integer idIncidencia;
    private String titulo;
    private List<Integer> estados;
    private Long creacionInicio;
    private Long creacionFinal;
}
