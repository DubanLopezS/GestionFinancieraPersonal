package com.fabrica.gestionfinancierapersonal.persistance.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fabrica.gestionfinancierapersonal.domain.enums.Rol;
import com.fabrica.gestionfinancierapersonal.domain.model.Usuario;

@Repository
public interface UsuarioJpaRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByUsername(String username);

    @Query("""
                SELECT u FROM Usuario u
                WHERE u.idUsuario <> :idAdmin
                AND u.rol <> :rol
            """)
    Page<Usuario> buscarUsuariosSinFiltro(
            @Param("idAdmin") UUID idAdmin,
            @Param("rol") Rol rol,
            Pageable pageable);

    @Query("""
                SELECT u FROM Usuario u
                WHERE u.idUsuario <> :idAdmin
                AND u.rol <> :rol
                AND (
                    LOWER(u.nombre) LIKE LOWER(CONCAT('%', :filtro, '%'))
                    OR LOWER(u.correo) LIKE LOWER(CONCAT('%', :filtro, '%'))
                    OR LOWER(u.username) LIKE LOWER(CONCAT('%', :filtro, '%'))
                )
            """)
    Page<Usuario> buscarUsuariosConFiltro(@Param("idAdmin") UUID idAdmin, @Param("rol") Rol rol,
            @Param("filtro") String filtro, Pageable pageable);

    boolean existsByCorreo(String correo);

    boolean existsByTelefono(String telefono);
}