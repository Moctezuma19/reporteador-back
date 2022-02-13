package com.jrivera.reporteador.seguridad.servicio;

import com.jrivera.reporteador.modelo.Usuario;

public interface UsuarioSeguridadServicio {
    Usuario findOne(String username);
}
