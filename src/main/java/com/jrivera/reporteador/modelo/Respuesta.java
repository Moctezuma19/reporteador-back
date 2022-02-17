package com.jrivera.reporteador.modelo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@Entity
public class Respuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRespuesta;
    private Integer idIncidencia;
    private Integer idUsuario;
    private String descripcion;
    private Timestamp actualizacion;
}
