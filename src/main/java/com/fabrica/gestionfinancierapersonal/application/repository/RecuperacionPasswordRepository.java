package com.fabrica.gestionfinancierapersonal.application.repository;

import java.util.Optional;

import com.fabrica.gestionfinancierapersonal.domain.model.RecuperacionPassword;

public interface RecuperacionPasswordRepository {

    RecuperacionPassword guardar(RecuperacionPassword recuperacion);

    Optional<RecuperacionPassword> buscarPorCodigo(String codigo);
}