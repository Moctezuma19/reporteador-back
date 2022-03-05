package com.jrivera.reporteador.repositorio;

import com.jrivera.reporteador.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {

    boolean existsByCorreoAndEliminadoIsFalse(String correo);

    @Query("select u from Usuario u where u.correo = ?1 and u.eliminado = false")
    Usuario findByCorreo(String correo);

    @Query("select u from Usuario u where u.rol.idRol = ?1 and u.eliminado = false")
    List<Usuario> findAllByRolIdRol(Integer idRol);

    List<Usuario> findAllByEliminadoIsFalse();
}
