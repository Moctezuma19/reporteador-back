package com.jrivera.reporteador.repositorio;

import com.jrivera.reporteador.modelo.Asignacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsignacionRepositorio extends JpaRepository<Asignacion, Integer> {
    List<Asignacion> findAllByIdUsuario(Integer idUsuario);

    Asignacion findByIdIncidencia(Integer idIncidencia);
}
