package com.jrivera.reporteador.modelo;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
public class Historial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHistorial;
    private Integer idAsignacion;
    private Timestamp actualizacion;
    @Column(insertable = false, updatable = false)
    private Integer idSolucion;
    @OneToOne
    @JoinColumn(referencedColumnName = "idSolucion", name = "idSolucion")
    private Solucion solucion;
}