package com.jrivera.reporteador.servicio.impl;

import com.jrivera.reporteador.dto.UsuarioDto;
import com.jrivera.reporteador.modelo.Rol;
import com.jrivera.reporteador.modelo.Usuario;
import com.jrivera.reporteador.repositorio.UsuarioRepositorio;
import com.jrivera.reporteador.servicio.UsuarioServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServicioImpl implements UsuarioServicio {

    private final Logger LOG = LoggerFactory.getLogger(UsuarioServicioImpl.class);
    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Override
    public Usuario guarda(UsuarioDto usuarioDto) {
        //LOG.info("Tratando de insertart -> " + usuarioDto);
        if (usuarioRepositorio.existsByCorreo(usuarioDto.getCorreo())) {
            LOG.info("devolvio nulo");
            return null;
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDto.getNombre());
        usuario.setApellido(usuarioDto.getApellido());
        usuario.setCorreo(usuarioDto.getCorreo());
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
        return usuario;
    }

    @Override
    public Usuario actualiza(UsuarioDto usuarioDto) {
        Usuario usuario = usuarioRepositorio.findById(usuarioDto.getIdUsuario()).orElse(null);
        if (usuario == null) {
            return null;
        }
        if (!usuario.getCorreo().equals(usuarioDto.getCorreo()) && usuarioRepositorio.existsByCorreo(usuarioDto.getCorreo())) {
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
        usuarioRepositorio.delete(usuario);
        return true;
    }

    @Override
    public List<Usuario> todos() {
        List<Usuario> usuarios = usuarioRepositorio.findAll();
        if (usuarios.size() == 0) {
            return null;
        }
        for (Usuario u : usuarios) {
            u.setPassword(null);
        }
        return usuarios;
    }
}
