package com.jrivera.reporteador.servicio;

import com.jrivera.reporteador.modelo.Incidencia;

public interface IncidenciaServicio {

    Incidencia crea(Incidencia incidencia);

    Boolean asigna(Integer idUsuario, Integer idIncidencia);

}
