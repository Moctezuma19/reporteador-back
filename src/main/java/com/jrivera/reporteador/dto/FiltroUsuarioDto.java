package com.jrivera.reporteador.dto;

import lombok.Data;

import java.util.List;

@Data
public class FiltroUsuarioDto {
    private String correo;
    private String nombre;
    private List<Integer> idRoles;
}
