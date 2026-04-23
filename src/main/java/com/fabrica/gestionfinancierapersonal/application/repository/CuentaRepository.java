package com.fabrica.gestionfinancierapersonal.application.repository;

import java.util.*;

import com.fabrica.gestionfinancierapersonal.domain.model.Cuenta;

public interface CuentaRepository {

    void guardar(Cuenta cuenta);

    Optional<Cuenta> buscarPorId(UUID id);

    List<Cuenta> buscarPorUsuario(UUID idUsuario);

    void actualizar(Cuenta cuenta);

    void eliminar(UUID id);
}
