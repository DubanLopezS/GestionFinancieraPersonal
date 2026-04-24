package com.fabrica.gestionfinancierapersonal.application.repository;

import java.util.*;

import com.fabrica.gestionfinancierapersonal.domain.enums.TipoTransaccion;
import com.fabrica.gestionfinancierapersonal.domain.model.Categoria;

public interface CategoriaRepository {

    void guardar(Categoria categoria);

    Optional<Categoria> buscarPorNombre(String nombre, UUID idUsuario);

    Optional<Categoria> buscarPorId(UUID id);

    List<Categoria> buscarPorUsuario(UUID idUsuario);

    void actualizar(Categoria categoria);

    void eliminar(UUID id);

    List<Categoria> buscarPorUsuarioYTipo(UUID idUsuario, TipoTransaccion tipo);

    Optional<Categoria> buscarPorIdYUsuario(UUID idCategoria, UUID idUsuario);

}

