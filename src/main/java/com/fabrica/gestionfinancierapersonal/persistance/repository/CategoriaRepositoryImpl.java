package com.fabrica.gestionfinancierapersonal.persistance.repository;

import java.util.*;

import org.springframework.stereotype.Repository;
import com.fabrica.gestionfinancierapersonal.application.repository.CategoriaRepository;
import com.fabrica.gestionfinancierapersonal.domain.enums.TipoTransaccion;
import com.fabrica.gestionfinancierapersonal.domain.model.Categoria;

@Repository
public class CategoriaRepositoryImpl implements CategoriaRepository {

    private final CategoriaJpaRepository jpaRepository;

    public CategoriaRepositoryImpl(CategoriaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void guardar(Categoria categoria) {
        jpaRepository.save(categoria);
    }

    @Override
    public Optional<Categoria> buscarPorNombre(String nombre, UUID idUsuario) {
        return jpaRepository.findByNombreAndUsuario_IdUsuario(nombre, idUsuario);
    }

    @Override
    public Optional<Categoria> buscarPorId(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Categoria> buscarPorUsuario(UUID idUsuario) {
        return jpaRepository.findByUsuario_IdUsuario(idUsuario);
    }

    @Override
    public void actualizar(Categoria categoria) {
        jpaRepository.save(categoria);
    }

    @Override
    public void eliminar(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Categoria> buscarPorUsuarioYTipo(UUID idUsuario, TipoTransaccion tipo) {
        return jpaRepository.findByUsuario_IdUsuarioAndTipo(idUsuario, tipo);
    }

    @Override
    public Optional<Categoria> buscarPorIdYUsuario(UUID idCategoria, UUID idUsuario) {
        return jpaRepository.findByIdCategoriaAndUsuario_IdUsuario(idCategoria, idUsuario);
    }

    @Override
    public Optional<Categoria> buscarPorNombreYEsSistemaTrue(String nombre) {
        return jpaRepository.findByNombreAndEsSistemaTrue(nombre);
    }
}