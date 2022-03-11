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
    private Integer idUsuario;
    private String imagen1;
    private String imagen2;
    @ManyToOne
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario", insertable = false, updatable = false)
    private Usuario usuario;
    private Integer estado;
    @OneToOne(mappedBy = "incidencia")
    private Asignacion asignacion;
}
