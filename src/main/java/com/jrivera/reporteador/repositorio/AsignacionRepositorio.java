package com.jrivera.reporteador.repositorio;

import com.jrivera.reporteador.modelo.Asignacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsignacionRepositorio extends JpaRepository<Asignacion, Integer> {
}
