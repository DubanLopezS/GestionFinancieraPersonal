package com.fabrica.gestionfinancierapersonal.persistance.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import com.fabrica.gestionfinancierapersonal.application.repository.RecuperacionPasswordRepository;
import com.fabrica.gestionfinancierapersonal.domain.model.RecuperacionPassword;

@Repository
public class RecuperacionPasswordRepositoryImpl implements RecuperacionPasswordRepository {

    private final RecuperacionPasswordJpaRepository repository;

    public RecuperacionPasswordRepositoryImpl(RecuperacionPasswordJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public RecuperacionPassword guardar(RecuperacionPassword recuperacion) {
        return repository.save(recuperacion);
    }

    @Override
    public Optional<RecuperacionPassword> buscarPorCodigo(String codigo) {
        return repository.findByCodigo(codigo);
    }

}