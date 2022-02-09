package com.jrivera.reporteador.modelo;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
public class Incidencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idIncidencia;
    @Column
    private String titulo;
    @Column
    private String descripcion;
    @Column
    private Timestamp creacion;
    @Column
    private Timestamp actualizacion;
    @Column
    private Timestamp cierre;
    @Column
    private Integer idUsuario;
    @Column
    private Integer estado;
    @Column
    private String solucion;
}
