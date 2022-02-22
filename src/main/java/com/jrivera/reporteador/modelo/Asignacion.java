package com.jrivera.reporteador.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Asignacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAsignacion;
    private Integer idUsuario;
    @ManyToOne
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario", insertable = false, updatable = false)
    private Usuario usuario;
    private Integer idIncidencia;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "idIncidencia", insertable = false, updatable = false)
    private Incidencia incidencia;

}
