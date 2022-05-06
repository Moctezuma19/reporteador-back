package com.jrivera.reporteador.controlador;

import com.jrivera.reporteador.servicio.ReporteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reporte")
public class ReporteControlador {
    @Autowired
    ReporteServicio reporteServicio;

    @GetMapping(value = "/obten/{idIncidencia}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] obtenReporte(@PathVariable Integer idIncidencia) {
        return reporteServicio.creaReporte(idIncidencia);
    }
}
