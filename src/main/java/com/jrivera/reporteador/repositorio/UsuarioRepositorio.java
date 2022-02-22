package com.jrivera.reporteador.repositorio;

import com.jrivera.reporteador.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {

    boolean existsByCorreo(String correo);

    Usuario findByCorreo(String correo);

    List<Usuario> findAllByRolIdRol(Integer idRol);
}
