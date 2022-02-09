package com.jrivera.reporteador.servicio.impl;

import com.jrivera.reporteador.dto.UsuarioDto;
import com.jrivera.reporteador.modelo.Rol;
import com.jrivera.reporteador.modelo.Usuario;
import com.jrivera.reporteador.repositorio.UsuarioRepositorio;
import com.jrivera.reporteador.servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class UsuarioServicioImpl implements UsuarioServicio {

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Override
    public Usuario guarda(UsuarioDto usuarioDto) {
        if (usuarioRepositorio.existsByCorreo(usuarioDto.getCorreo())) {
            return null;
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDto.getNombre());
        usuario.setApellido(usuarioDto.getApellido());
        usuario.setCorreo(usuarioDto.getCorreo());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        usuario.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));
        Rol rol = new Rol();
        rol.setIdRol(usuarioDto.getIdRol());
        usuario.setRol(rol);
        return usuarioRepositorio.saveAndFlush(usuario);
    }

    @Override
    public Usuario actualiza(UsuarioDto usuarioDto) {
        Usuario usuario = usuarioRepositorio.findById(usuarioDto.getIdUsuario()).orElse(null);
        if (usuario == null) {
            return null;
        }
        usuario.setNombre(usuarioDto.getNombre());
        usuario.setApellido(usuarioDto.getApellido());
        usuario.setCorreo(usuarioDto.getCorreo());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        usuario.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));
        Rol rol = new Rol();
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
