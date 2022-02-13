package com.jrivera.reporteador.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UsuarioDto {
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private String password;
    private Integer idRol;
    private String token;
}
