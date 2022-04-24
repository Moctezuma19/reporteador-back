package com.jrivera.reporteador.servicio;

import com.jrivera.reporteador.dto.UsuarioDto;
import com.jrivera.reporteador.modelo.Usuario;

import java.util.List;

public interface UsuarioServicio {

    List<Integer> todos();

    Usuario guarda(UsuarioDto usuarioDto);

    Usuario actualiza(UsuarioDto usuarioDto);

    Boolean elimina(Integer idUsuario);
}
