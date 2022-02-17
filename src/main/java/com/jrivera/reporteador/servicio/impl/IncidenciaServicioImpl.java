package com.jrivera.reporteador.servicio.impl;

import com.jrivera.reporteador.dto.IncidenciaDto;
import com.jrivera.reporteador.modelo.Asignacion;
import com.jrivera.reporteador.modelo.Incidencia;
import com.jrivera.reporteador.modelo.Usuario;
import com.jrivera.reporteador.repositorio.AsignacionRepositorio;
import com.jrivera.reporteador.repositorio.IncidenciaRepositorio;
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

    @Override
    public Incidencia crea(IncidenciaDto incidenciaDto) {
        Incidencia incidencia = new Incidencia();
        incidencia.setIdUsuario(incidenciaDto.getIdUsuario());
        incidencia.setTitulo(incidenciaDto.getTitulo());
        incidencia.setDescripcion(incidencia.getDescripcion());
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        incidencia.setCreacion(currentTime);
        incidencia.setActualizacion(currentTime);
        incidencia.setEstado(0);
        return incidenciaRepositorio.save(incidencia);
    }

    @Override
    public Boolean asigna(Integer idUsuario, Integer idIncidencia) {
        if (!incidenciaRepositorio.existsById(idIncidencia)) {
            return false;
        }
        if (!usuarioRepositorio.existsById(idUsuario)) {
            return false;
        }

        Asignacion asignacion = new Asignacion();
        asignacion.setIdIncidencia(idIncidencia);
        asignacion.setIdUsuario(idUsuario);
        asignacionRepositorio.save(asignacion);
        return true;
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
}
