package com.jrivera.reporteador.modelo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private String password;
    private Boolean eliminado;

    @ManyToOne
    @JoinColumn(name = "idRol")
    private Rol rol;
}
