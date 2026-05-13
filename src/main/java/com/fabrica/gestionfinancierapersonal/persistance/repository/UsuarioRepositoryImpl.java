package com.fabrica.gestionfinancierapersonal.persistance.repository;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.fabrica.gestionfinancierapersonal.application.repository.UsuarioRepository;
import com.fabrica.gestionfinancierapersonal.domain.enums.Rol;
import com.fabrica.gestionfinancierapersonal.domain.model.Usuario;

@Repository
public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final UsuarioJpaRepository jpaRepository;

    public UsuarioRepositoryImpl(UsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void guardar(Usuario usuario) {
        jpaRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return jpaRepository.findByCorreo(correo);
    }

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        return jpaRepository.findByUsername(username);
    }

    @Override
    public List<Usuario> buscarTodos() {
        return jpaRepository.findAll();
    }

    @Override
    public void actualizar(Usuario usuario) {
        jpaRepository.save(usuario);
    }

    @Override
    public void eliminar(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Page<Usuario> buscarUsuarios(
            UUID idAdmin,
            String filtro,
            Pageable pageable) {
        if (filtro == null || filtro.isBlank()) {
            return jpaRepository.buscarUsuariosSinFiltro(
                    idAdmin,
                    Rol.ADMIN,
                    pageable);
        }
        return jpaRepository.buscarUsuariosConFiltro(
                idAdmin,
                Rol.ADMIN,
                filtro,
                pageable);
    }
}
