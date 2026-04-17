package com.fabrica.gestionfinancierapersonal.persistance.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import com.fabrica.gestionfinancierapersonal.application.repository.CategoriaRepository;
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
}