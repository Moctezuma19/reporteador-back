package com.jrivera.reporteador.modelo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Asignacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAsignacion;
    private Integer idUsuario;
    private Integer idIncidencia;

}
