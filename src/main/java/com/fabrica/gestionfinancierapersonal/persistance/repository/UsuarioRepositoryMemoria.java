package com.fabrica.gestionfinancierapersonal.persistance.repository;

import java.util.*;

import org.springframework.stereotype.Repository;
import com.fabrica.gestionfinancierapersonal.application.repository.UsuarioRepository;
import com.fabrica.gestionfinancierapersonal.domain.model.Usuario;

@Repository
public class UsuarioRepositoryMemoria implements UsuarioRepository {

    private final Map<String, Usuario> usuarios = new HashMap<>();

    @Override
    public void guardar(Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
    }

    @Override
    public Usuario buscarPorId(String id) {
        return usuarios.get(id);
    }

    @Override
    public Usuario buscarPorCorreo(String correo) {
        return usuarios.values()
                .stream()
                .filter(u -> u.getCorreo().equals(correo))
                .findFirst()
                .orElse(null);
    }
}
