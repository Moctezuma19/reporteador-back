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
    private String titulo;
    private String descripcion;
    private Timestamp creacion;
    private Timestamp actualizacion;
    private Timestamp cierre;
    private Integer idUsuario;
    private Integer estado;
    @Column(insertable = false, updatable = false)
    private Integer idSolucion;
    @OneToOne
    @JoinColumn(referencedColumnName = "idSolucion", name = "idSolucion")
    private Solucion solucion;
}
