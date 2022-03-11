package com.jrivera.reporteador.controlador;

import com.jrivera.reporteador.dto.IncidenciaDto;
import com.jrivera.reporteador.dto.RespuestaDto;
import com.jrivera.reporteador.modelo.Incidencia;
import com.jrivera.reporteador.modelo.Respuesta;
import com.jrivera.reporteador.modelo.Usuario;
import com.jrivera.reporteador.repositorio.IncidenciaRepositorio;
import com.jrivera.reporteador.repositorio.RespuestaRepositorio;
import com.jrivera.reporteador.servicio.ImagenServicio;
import com.jrivera.reporteador.servicio.IncidenciaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incidencia")
public class IncidenciaControlador {

    @Autowired
    IncidenciaServicio incidenciaServicio;
    @Autowired
    IncidenciaRepositorio incidenciaRepositorio;
    @Autowired
    RespuestaRepositorio respuestaRepositorio;
    @Autowired
    ImagenServicio imagenServicio;

    @PutMapping(path = "/crea", consumes = {"multipart/form-data"})
    public Incidencia crea(@ModelAttribute IncidenciaDto incidenciaDto) {
        return incidenciaServicio.crea(incidenciaDto);
    }

    @GetMapping("/asigna/{idUsuario}")
    public Usuario asigna(@PathVariable Integer idUsuario, @RequestParam("id") Integer idIncidencia) {
        return incidenciaServicio.asigna(idUsuario, idIncidencia);
    }

    @GetMapping("/todas/{idUsuario}")
    public List<Incidencia> incidencias(@PathVariable Integer idUsuario) {
        return incidenciaServicio.obtenTodas(idUsuario);
    }

    @GetMapping("/respuestas/{idIncidencia}")
    public List<Respuesta> obtenRespuestas(@PathVariable Integer idIncidencia) {
        return respuestaRepositorio.findAllByIdIncidencia(idIncidencia);
    }

    @PutMapping("/responde")
    public Respuesta responde(@RequestBody RespuestaDto respuestaDto) {
        return incidenciaServicio.crea(respuestaDto);
    }

    @GetMapping("/descripcion/{idIncidencia}")
    public String descripcion(@PathVariable Integer idIncidencia) {
        return incidenciaRepositorio.findDescripcionByIdIncidencia(idIncidencia);
    }


}
