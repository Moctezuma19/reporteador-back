package com.jrivera.reporteador.controlador;

import com.jrivera.reporteador.modelo.Incidencia;
import com.jrivera.reporteador.servicio.IncidenciaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/incidencia")
public class IncidenciaControlador {

    @Autowired
    IncidenciaServicio incidenciaServicio;

    @PutMapping("/crea")
    public Incidencia crea(@RequestBody Incidencia incidencia) {
        return null;
        //return incidenciaServicio.crea(incidencia);
    }

    @GetMapping("/asigna/{idUsuario}")
    public Boolean asigna(@PathVariable Integer idUsuario, @RequestParam("id") Integer idIncidencia) {
        return incidenciaServicio.asigna(idUsuario, idIncidencia);
    }

}
