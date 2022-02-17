package com.jrivera.reporteador.controlador;

import com.jrivera.reporteador.dto.IncidenciaDto;
import com.jrivera.reporteador.modelo.Incidencia;
import com.jrivera.reporteador.repositorio.IncidenciaRepositorio;
import com.jrivera.reporteador.servicio.IncidenciaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incidencia")
public class IncidenciaControlador {

    @Autowired
    IncidenciaServicio incidenciaServicio;

    @PutMapping("/crea")
    public Incidencia crea(@RequestBody IncidenciaDto incidenciaDto) {
        return incidenciaServicio.crea(incidenciaDto);
    }

    @GetMapping("/asigna/{idUsuario}")
    public Boolean asigna(@PathVariable Integer idUsuario, @RequestParam("id") Integer idIncidencia) {
        return incidenciaServicio.asigna(idUsuario, idIncidencia);
    }

    @GetMapping("/todas/{idUsuario}")
    public List<Incidencia> incidencias(@PathVariable Integer idUsuario) {
       return incidenciaServicio.obtenTodas(idUsuario);
    }

}
