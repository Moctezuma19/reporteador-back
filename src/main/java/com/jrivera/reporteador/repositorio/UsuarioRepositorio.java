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

    @Query("select u from Usuario u where u.eliminado = false and concat(u.nombre, concat(' ', u.apellido)) like concat('%', concat(?1,'%') ) and u.correo like concat('%',concat(?2,'%')) and u.rol.idRol in (?3)")
    List<Usuario> findAllByFilters(String nombre, String correo, List<Integer> idRoles);

    @Query("select u.correo from Usuario u where u.idUsuario =?1")
    String findCorreoByIdUsuario(Integer idUsuario);

   /* @Query("select u from Usuario u where u.rol.idRol in (?1) and u.eliminado = false")
    List<Usuario> findAllByIdRoles(List<Integer> idRoles);

    @Query("select u from Usuario u where u.eliminado = false and u.correo like concat('%',concat(?1,'%')) and u.rol.idRol in (?2)")
    List<Usuario> findAllByCorreoLikeAndIdRoles(String correoLike, List<Integer> idRoles);

    @Query("select u from Usuario u where u.eliminado = false and concat(u.nombre, concat(' ', u.apellido)) like concat('%', concat(?1,'%')) and u.rol.idRol in (?3)")
    List<Usuario> findAllByNombreLikeAndIdRoles(String nombreLike, List<Integer> idRoles);*/
}
