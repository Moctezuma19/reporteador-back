package com.jrivera.reporteador.modelo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Rol {
    @Id
    private Integer idRol;
    private String nombre;
}
