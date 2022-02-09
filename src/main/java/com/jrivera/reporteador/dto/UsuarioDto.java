package com.jrivera.reporteador.dto;

import lombok.Data;

@Data
public class UsuarioDto {
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private String password;
    private Integer idRol;
}
