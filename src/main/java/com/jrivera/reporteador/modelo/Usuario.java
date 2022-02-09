package com.jrivera.reporteador.modelo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;
    @Column
    private String nombre;
    @Column
    private String apellido;
    @Column
    private String correo;
    @Column
    private String password;

    @ManyToOne
    @JoinColumn(name = "idRol")
    private Rol rol;
}
