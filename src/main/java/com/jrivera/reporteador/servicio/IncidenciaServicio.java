package com.jrivera.reporteador.servicio;

import com.jrivera.reporteador.dto.IncidenciaDto;
import com.jrivera.reporteador.modelo.Incidencia;

import java.util.List;

public interface IncidenciaServicio {

    Incidencia crea(IncidenciaDto incidenciaDto);

    Boolean asigna(Integer idUsuario, Integer idIncidencia);

    List<Incidencia> obtenTodas(Integer idUsuario);

}
