package com.jrivera.reporteador.servicio;

public interface CorreoServicio {
    void enviaCorreo(String asunto, String contenido, String destinatario);
}
