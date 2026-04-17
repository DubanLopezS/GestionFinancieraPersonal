package com.fabrica.gestionfinancierapersonal.application.repository;

import java.util.Optional;
import java.util.UUID;

import com.fabrica.gestionfinancierapersonal.domain.model.Categoria;

public interface CategoriaRepository {

    void guardar(Categoria categoria);

    Optional<Categoria> buscarPorNombre(String nombre, UUID idUsuario);
}

