package com.fabrica.gestionfinancierapersonal.persistance.repository;

import java.util.*;

import org.springframework.stereotype.Repository;
import com.fabrica.gestionfinancierapersonal.application.repository.UsuarioRepository;
import com.fabrica.gestionfinancierapersonal.domain.model.Usuario;

@Repository
public class UsuarioRepositoryMemoria implements UsuarioRepository {

    private final Map<UUID, Usuario> usuarios = new HashMap<>();

    @Override
    public void guardar(Usuario usuario) {
        usuarios.put(usuario.getIdUsuario(), usuario);
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID idUsuario) {
        return Optional.ofNullable(usuarios.get(idUsuario));
    }

    @Override
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarios.values()
                .stream()
                .filter(u -> u.getCorreo().equals(correo))
                .findFirst();
    }

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarios.values()
                .stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }
}
