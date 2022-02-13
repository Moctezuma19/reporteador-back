package com.jrivera.reporteador.modelo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Solucion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSolucion;
    private String descripcion;
    private String img_ruta1;
    private String img_ruta2;
    private String img_ruta3;
    private String img_ruta4;
}
