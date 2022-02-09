package com.jrivera.reporteador.servicio.impl;

import com.jrivera.reporteador.modelo.Asignacion;
import com.jrivera.reporteador.modelo.Incidencia;
import com.jrivera.reporteador.repositorio.AsignacionRepositorio;
import com.jrivera.reporteador.repositorio.IncidenciaRepositorio;
import com.jrivera.reporteador.repositorio.UsuarioRepositorio;
import com.jrivera.reporteador.servicio.IncidenciaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class IncidenciaServicioImpl implements IncidenciaServicio {

    @Autowired
    IncidenciaRepositorio incidenciaRepositorio;

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Autowired
    AsignacionRepositorio asignacionRepositorio;

    @Override
    public Incidencia crea(Incidencia incidencia) {
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
}
