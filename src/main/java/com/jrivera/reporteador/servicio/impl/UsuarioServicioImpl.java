package com.jrivera.reporteador.servicio.impl;

import com.jrivera.reporteador.dto.UsuarioDto;
import com.jrivera.reporteador.modelo.Asignacion;
import com.jrivera.reporteador.modelo.Incidencia;
import com.jrivera.reporteador.modelo.Rol;
import com.jrivera.reporteador.modelo.Usuario;
import com.jrivera.reporteador.repositorio.AsignacionRepositorio;
import com.jrivera.reporteador.repositorio.IncidenciaRepositorio;
import com.jrivera.reporteador.repositorio.UsuarioRepositorio;
import com.jrivera.reporteador.servicio.CorreoServicio;
import com.jrivera.reporteador.servicio.UsuarioServicio;
import com.jrivera.reporteador.util.Textos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServicioImpl implements UsuarioServicio {

    private final Logger LOG = LoggerFactory.getLogger(UsuarioServicioImpl.class);
    @Autowired
    UsuarioRepositorio usuarioRepositorio;
    @Autowired
    AsignacionRepositorio asignacionRepositorio;
    @Autowired
    IncidenciaRepositorio incidenciaRepositorio;
    @Autowired
    CorreoServicio correoServicio;
    @Value("${reporteador.url}")
    private String servidorUrl;

    @Override
    public Usuario guarda(UsuarioDto usuarioDto) {
        if (usuarioRepositorio.existsByCorreoAndEliminadoIsFalse(usuarioDto.getCorreo())) {
            LOG.info("devolvio nulo");
            return null;
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDto.getNombre());
        usuario.setApellido(usuarioDto.getApellido());
        usuario.setCorreo(usuarioDto.getCorreo());
        usuario.setEliminado(false);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        usuario.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));
        Rol rol = new Rol();
        if (usuarioDto.getIdRol() == 2) {
            rol.setNombre("ingeniero");
        } else {
            rol.setNombre("usuario");
        }
        rol.setIdRol(usuarioDto.getIdRol());

        usuario.setRol(rol);
        usuario = usuarioRepositorio.saveAndFlush(usuario);
        String contenido = String.format(Textos.BIENVENIDA, usuario.getCorreo(), usuarioDto.getPassword(), servidorUrl + "/", servidorUrl + "/");
        correoServicio.enviaCorreo("Bienvenido", contenido, usuario.getCorreo());
        return usuario;
    }

    @Override
    public Usuario actualiza(UsuarioDto usuarioDto) {
        Usuario usuario = usuarioRepositorio.findById(usuarioDto.getIdUsuario()).orElse(null);
        if (usuario == null) {
            return null;
        }
        if (!usuario.getCorreo().equals(usuarioDto.getCorreo()) && usuarioRepositorio.existsByCorreoAndEliminadoIsFalse(usuarioDto.getCorreo())) {
            LOG.info("devolvio nulo");
            return null;
        }
        usuario.setNombre(usuarioDto.getNombre());
        usuario.setApellido(usuarioDto.getApellido());
        usuario.setCorreo(usuarioDto.getCorreo());
        if (usuarioDto.getPassword().length() > 0) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            usuario.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));
        }
        Rol rol = new Rol();
        if (usuarioDto.getIdRol() == 2) {
            rol.setNombre("ingeniero");
        } else if (usuarioDto.getIdRol() == 3) {
            rol.setNombre("usuario");
        } else {
            rol.setNombre("administrador");
        }
        rol.setIdRol(usuarioDto.getIdRol());
        usuario.setRol(rol);
        return usuarioRepositorio.saveAndFlush(usuario);
    }

    @Override
    public Boolean elimina(Integer idUsuario) {
        Usuario usuario = usuarioRepositorio.findById(idUsuario).orElse(null);
        if (usuario == null) {
            return false;
        }
        usuario.setEliminado(true);
        if (usuario.getRol().getIdRol().equals(2)) {
            List<Asignacion> asignaciones = asignacionRepositorio.findAllByIdUsuario(usuario.getIdUsuario());
            List<Incidencia> incidencias = incidenciaRepositorio.findAllByIdAsignado_(usuario.getIdUsuario());
            for (Asignacion a : asignaciones) {
                asignacionRepositorio.delete(a);
            }
            for (Incidencia i : incidencias) {
                if (!i.getEstado().equals(2)) {
                    i.setEstado(6);
                    incidenciaRepositorio.save(i);
                }
            }

        } else {
            List<Integer> incidencias = incidenciaRepositorio.findAllByIdUsuario(usuario.getIdUsuario());
            for (Integer i : incidencias) {
                /*Asignacion asignacion = asignacionRepositorio.findByIdIncidencia(i.getIdIncidencia());
                asignacionRepositorio.delete(asignacion);*/
                incidenciaRepositorio.deleteById(i);
            }
        }
        usuarioRepositorio.save(usuario);

        return true;
    }

    @Override
    public List<Integer> todos() {
        List<Integer> usuarios = usuarioRepositorio.findAllByEliminadoIsFalse();
        if (usuarios.size() == 0) {
            return null;
        }
        return usuarios;
    }
}
