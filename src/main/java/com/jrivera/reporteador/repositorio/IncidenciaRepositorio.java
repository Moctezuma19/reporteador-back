package com.jrivera.reporteador.repositorio;

import com.jrivera.reporteador.modelo.Incidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidenciaRepositorio extends JpaRepository<Incidencia, Integer> {

    List<Incidencia> findAllByIdUsuario(Integer idUsuario);

    @Query("select i from Incidencia i where i.idIncidencia in (select a.idIncidencia from Asignacion a where a.idUsuario = ?1)")
    List<Incidencia> findAllByIdAsignado(Integer idAsignado);

}
