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
import com.jrivera.reporteador.servicio.CorreoServicio;
import com.jrivera.reporteador.servicio.ImagenServicio;
import com.jrivera.reporteador.servicio.IncidenciaServicio;
import com.jrivera.reporteador.util.Textos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

    @Autowired
    ImagenServicio imagenServicio;

    @Autowired
    CorreoServicio correoServicio;

    @Value("${reporteador.admin.correo}")
    private String correoAdmin;

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
        incidencia.setDescripcion(incidenciaDto.getDescripcion());
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        incidencia.setCreacion(currentTime);
        incidencia.setActualizacion(currentTime);
        incidencia.setEstado(0);
        incidencia = incidenciaRepositorio.save(incidencia);

        if (incidenciaDto.getImagen1() != null) {
            String h = imagenServicio.guardaImagen(incidencia.getIdIncidencia(), incidenciaDto.getImagen1());
            incidencia.setImagen1(h);
        }

        if (incidenciaDto.getImagen2() != null) {
            String h = imagenServicio.guardaImagen(incidencia.getIdIncidencia(), incidenciaDto.getImagen2());
            incidencia.setImagen2(h);
        }

        incidencia = incidenciaRepositorio.save(incidencia);

        String fecha = new SimpleDateFormat("dd/MM/yyyy").format(currentTime);
        String hora = new SimpleDateFormat("HH:mm").format(currentTime);
        String contenido = String.format(Textos.INCIDENCIA_CREADA, usuario.getNombre() + " " + usuario.getApellido(),
                usuario.getCorreo(), fecha, hora);
        correoServicio.enviaCorreo("Nueva incidencia", contenido, correoAdmin);
        return incidencia;
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

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        String fecha = new SimpleDateFormat("dd/MM/yyyy").format(currentTime);
        String hora = new SimpleDateFormat("HH:mm").format(currentTime);
        String contenido = String.format(Textos.ASIGNACION_PARA_INGENIERO, idIncidencia, fecha, hora);
        String contenido_ = String.format(Textos.ASIGNACION_PARA_USUARIO, idIncidencia, fecha, hora);
        correoServicio.enviaCorreo("Asignación", contenido, usuario.getCorreo());
        correoServicio.enviaCorreo("Ingeniero de servicio asignado", contenido_, incidencia.getUsuario().getCorreo());
        return usuario;
    }

    @Override
    public List<Integer> obtenTodas(Integer idUsuario) {
        Usuario usuario = usuarioRepositorio.findById(idUsuario).orElse(null);
        if (usuario == null) {
            return null;
        }

        List<Integer> incidencias = null;

        switch (usuario.getRol().getIdRol()) {
            case 1:
                incidencias = incidenciaRepositorio.findAllIdIncidencia();
                break;
            case 2:
                incidencias = incidenciaRepositorio.findAllByIdAsignado(idUsuario);
                break;
            case 3:
                incidencias = incidenciaRepositorio.findAllByIdUsuario(idUsuario);
        }

        return incidencias;

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
        if (respuestaDto.getEstado() != 0) {
            incidencia.setEstado(respuestaDto.getEstado());
        }
        incidenciaRepositorio.save(incidencia);

        String destinatario = incidencia.getUsuario().getIdUsuario().equals(respuesta.getIdUsuario()) ?
                incidencia.getUsuario().getCorreo() : usuarioRepositorio.findCorreoByIdUsuario(respuesta.getIdUsuario());

        String fecha = new SimpleDateFormat("dd/MM/yyyy").format(t);
        String hora = new SimpleDateFormat("HH:mm").format(t);
        String contenido = String.format(Textos.RESPUESTA_RECIBIDA, respuesta.getIdIncidencia(), fecha, hora);
        correoServicio.enviaCorreo("Respuesta recibida", contenido, destinatario);
        return respuesta;
    }
}
