package com.jrivera.reporteador.controlador;

import com.jrivera.reporteador.dto.FiltroUsuarioDto;
import com.jrivera.reporteador.dto.UsuarioDto;
import com.jrivera.reporteador.modelo.Usuario;
import com.jrivera.reporteador.repositorio.UsuarioRepositorio;
import com.jrivera.reporteador.servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioControlador {
    @Autowired
    UsuarioServicio usuarioServicio;

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @PutMapping("/registra")
    public Usuario registra(@RequestBody UsuarioDto usuarioDto) {
        return usuarioServicio.guarda(usuarioDto);
    }

    @PostMapping("/actualiza")
    public Usuario actualiza(@RequestBody UsuarioDto usuarioDto) {
        return usuarioServicio.actualiza(usuarioDto);
    }

    @GetMapping("/elimina/{idUsuario}")
    public Boolean elimina(@PathVariable Integer idUsuario) {
        return usuarioServicio.elimina(idUsuario);
    }

    @GetMapping("/todos")
    public List<Usuario> todos() {
        return usuarioServicio.todos();
    }

    @GetMapping("/ingenieros")
    public List<Usuario> ingenieros() {
        return usuarioRepositorio.findAllByRolIdRol(2);
    }

    @PostMapping("/filtra")
    public List<Usuario> filtra(@RequestBody FiltroUsuarioDto filtroUsuarioDto) {
        return usuarioRepositorio.findAllByFilters(filtroUsuarioDto.getNombre(), filtroUsuarioDto.getCorreo(), filtroUsuarioDto.getIdRoles());
    }


}
