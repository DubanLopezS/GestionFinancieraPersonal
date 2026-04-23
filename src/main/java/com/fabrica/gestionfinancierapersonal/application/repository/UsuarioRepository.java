package com.fabrica.gestionfinancierapersonal.application.repository;

import java.util.*;

import com.fabrica.gestionfinancierapersonal.domain.model.Usuario;

public interface UsuarioRepository {

    void guardar(Usuario usuario);

    Optional<Usuario> buscarPorId(UUID id);

    Optional<Usuario> buscarPorCorreo(String correo);

    Optional<Usuario> buscarPorUsername(String username);

    List<Usuario> buscarTodos();

    void actualizar(Usuario usuario);

    void eliminar(UUID id);
}