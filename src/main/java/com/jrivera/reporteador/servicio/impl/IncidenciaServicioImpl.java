package com.jrivera.reporteador.servicio.impl;

import com.jrivera.reporteador.dto.IncidenciaDto;
import com.jrivera.reporteador.dto.RespuestaDto;
import com.jrivera.reporteador.modelo.Asignacion;
import com.jrivera.reporteador.modelo.Incidencia;
import com.jrivera.reporteador.modelo.Respuesta;
import com.jrivera.reporteador.modelo.Usuario;
import com.jrivera.reporteador.repositorio.AsignacionRepositorio;
import com.jrivera.reporteador.repositorio.IncidenciaRepositorio;
import com.jrivera.reporteador.repositorio.RespuestaRepositorio;
import com.jrivera.reporteador.repositorio.UsuarioRepositorio;
import com.jrivera.reporteador.servicio.IncidenciaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class IncidenciaServicioImpl implements IncidenciaServicio {

    @Autowired
    IncidenciaRepositorio incidenciaRepositorio;

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Autowired
    AsignacionRepositorio asignacionRepositorio;

    @Autowired
    RespuestaRepositorio respuestaRepositorio;

    @Override
    public Incidencia crea(IncidenciaDto incidenciaDto) {
        Usuario usuario = usuarioRepositorio.findById(incidenciaDto.getIdUsuario()).orElse(null);
        if (usuario == null) {
            return null;
        }
        Incidencia incidencia = new Incidencia();
        incidencia.setIdUsuario(incidenciaDto.getIdUsuario());
        incidencia.setUsuario(usuario);
        incidencia.setTitulo(incidenciaDto.getTitulo());
        incidencia.setDescripcion(incidencia.getDescripcion());
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        incidencia.setCreacion(currentTime);
        incidencia.setActualizacion(currentTime);
        incidencia.setEstado(0);
        return incidenciaRepositorio.save(incidencia);
    }

    @Override
    public Usuario asigna(Integer idUsuario, Integer idIncidencia) {
        Incidencia incidencia = incidenciaRepositorio.findById(idIncidencia).orElse(null);
        if (incidencia == null) {
            return null;
        }
        Usuario usuario = usuarioRepositorio.findById(idUsuario).orElse(null);
        if (usuario == null) {
            return null;
        }

        Asignacion asignacion = new Asignacion();
        asignacion.setIdIncidencia(idIncidencia);
        asignacion.setIdUsuario(idUsuario);
        asignacion.setUsuario(usuario);
        asignacionRepositorio.save(asignacion);
        incidencia.setEstado(1);
        incidenciaRepositorio.save(incidencia);
        return usuario;
    }

    @Override
    public List<Incidencia> obtenTodas(Integer idUsuario) {
        Usuario usuario = usuarioRepositorio.findById(idUsuario).orElse(null);
        if (usuario == null) {
            return null;
        }

        switch (usuario.getRol().getIdRol()) {
            case 1:
                return incidenciaRepositorio.findAll();
            case 2:
                return incidenciaRepositorio.findAllByIdAsignado(idUsuario);
            case 3:
                return incidenciaRepositorio.findAllByIdUsuario(idUsuario);
            default:
                return null;
        }
    }

    @Override
    public Respuesta crea(RespuestaDto respuestaDto) {
        Incidencia incidencia = incidenciaRepositorio.findById(respuestaDto.getIdIncidencia()).orElse(null);
        if (incidencia == null) {
            return null;
        }
        Respuesta respuesta = new Respuesta();
        respuesta.setIdIncidencia(respuestaDto.getIdIncidencia());
        respuesta.setIdUsuario(respuestaDto.getIdUsuario());
        respuesta.setDescripcion(respuestaDto.getDescripcion());
        Timestamp t = new Timestamp(System.currentTimeMillis());
        respuesta.setActualizacion(t);
        respuesta = respuestaRepositorio.save(respuesta);

        incidencia.setActualizacion(t);
        if (respuestaDto.getCierre()) {
            incidencia.setEstado(2);
        }
        incidenciaRepositorio.save(incidencia);

        return respuesta;
    }
}
