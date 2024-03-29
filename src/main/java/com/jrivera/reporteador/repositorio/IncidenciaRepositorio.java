package com.jrivera.reporteador.repositorio;

import com.jrivera.reporteador.modelo.Incidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface IncidenciaRepositorio extends JpaRepository<Incidencia, Integer> {

    @Query("select i.idIncidencia from Incidencia i where i.idUsuario = ?1")
    List<Integer> findAllByIdUsuario(Integer idUsuario);

    @Query("select i.idIncidencia from Incidencia i where i.idIncidencia in (select a.idIncidencia from Asignacion a where a.idUsuario = ?1)")
    List<Integer> findAllByIdAsignado(Integer idAsignado);

    @Query("select i from Incidencia i where i.idIncidencia in (select a.idIncidencia from Asignacion a where a.idUsuario = ?1)")
    List<Incidencia> findAllByIdAsignado_(Integer idAsignado);

    @Query("select i.descripcion from Incidencia i where i.idIncidencia = ?1")
    String findDescripcionByIdIncidencia(Integer idIncidencia);

    //falta nombre de usuario e ingeniero
    @Query("select i.idIncidencia from Incidencia i where i.titulo like concat('%', concat(?1, '%'))"
            + "and i.estado in (?2) and i.creacion >= ?3 and i.creacion <= ?4")
    List<Integer> findAllByFilters(String titulo, List<Integer> estados, Timestamp creacionInicio, Timestamp creacionFinal);

    Incidencia findIncidenciaByIdIncidencia(Integer idIncidencia);

    @Query("select i.idIncidencia from Incidencia i")
    List<Integer> findAllIdIncidencia();

}
