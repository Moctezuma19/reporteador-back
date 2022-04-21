package com.jrivera.reporteador.util;

public interface Textos {
    String BIENVENIDA = "<p>Ha sido registrado en el reporteador de incidencias "
            + "con el correo <b>%s</b> y contraseña <b>%s</b></p><p>Puede acceder al sitio a través del siguiente enlace: <a href='%s'>%s</a>";
    String INCIDENCIA_CREADA = "El usuario %s con correo %s ha creado una incidencia el día %s a las %s.";
    String ASIGNACION_PARA_INGENIERO = "Ha sido asignado a la incidencia con identificador %s el día %s a las %s.";
    String ASIGNACION_PARA_USUARIO = "Se le ha asignado un ingeniero de servicio a su incidencia con identificador %s el día %s a las %s.";
    String RESPUESTA_RECIBIDA = "Ha recibido una respuesta a la incidencia con identificador %s el día %s a las %s.";
}
