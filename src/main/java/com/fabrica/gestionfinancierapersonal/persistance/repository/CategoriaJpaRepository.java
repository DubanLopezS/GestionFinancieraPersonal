package com.fabrica.gestionfinancierapersonal.persistance.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fabrica.gestionfinancierapersonal.domain.model.Categoria;

@Repository
public interface CategoriaJpaRepository extends JpaRepository<Categoria, UUID> {

    Optional<Categoria> findByNombreAndUsuario_IdUsuario(String nombre, UUID idUsuario);

    List<Categoria> findByUsuario_IdUsuario(UUID idUsuario);
}