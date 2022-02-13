package com.jrivera.reporteador.seguridad.servicio.impl;

import com.jrivera.reporteador.modelo.Usuario;
import com.jrivera.reporteador.repositorio.UsuarioRepositorio;
import com.jrivera.reporteador.seguridad.modelo.UsuarioPrincipal;
import com.jrivera.reporteador.seguridad.servicio.UsuarioSeguridadServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioSeguridadServicioImpl implements UsuarioSeguridadServicio, UserDetailsService {

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Override
    public UsuarioPrincipal loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.findByCorreo(correo);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado!");
        }
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
        List<GrantedAuthority> listAuth = new ArrayList<>();
        listAuth.add(grantedAuthority);

        UsuarioPrincipal usuarioPrincipal = new UsuarioPrincipal(usuario.getCorreo(), usuario.getPassword(), listAuth);
        usuarioPrincipal.setIdUser(usuario.getIdUsuario());
        return usuarioPrincipal;
    }

    @Override
    public Usuario findOne(String username) {
        return usuarioRepositorio.findByCorreo(username);
    }
}
