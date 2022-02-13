package com.jrivera.reporteador.repositorio;

import com.jrivera.reporteador.modelo.Solucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolucionRepositorio extends JpaRepository<Solucion, Integer> {
}
