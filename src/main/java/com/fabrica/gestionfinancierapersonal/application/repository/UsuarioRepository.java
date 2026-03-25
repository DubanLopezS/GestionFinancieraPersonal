package com.fabrica.gestionfinancierapersonal.application.repository;

import com.fabrica.gestionfinancierapersonal.domain.model.Usuario;

public interface UsuarioRepository {

    void guardar(Usuario usuario);

    Usuario buscarPorId(String id);

    Usuario buscarPorCorreo(String correo);
}