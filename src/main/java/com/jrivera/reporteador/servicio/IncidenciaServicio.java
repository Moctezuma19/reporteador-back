package com.jrivera.reporteador.servicio;

import com.jrivera.reporteador.dto.IncidenciaDto;
import com.jrivera.reporteador.dto.RespuestaDto;
import com.jrivera.reporteador.modelo.Incidencia;
import com.jrivera.reporteador.modelo.Respuesta;
import com.jrivera.reporteador.modelo.Usuario;

import java.util.List;

public interface IncidenciaServicio {

    Incidencia crea(IncidenciaDto incidenciaDto);

    Usuario asigna(Integer idUsuario, Integer idIncidencia);

    List<Incidencia> obtenTodas(Integer idUsuario);

    Respuesta crea(RespuestaDto respuestaDto);

}
