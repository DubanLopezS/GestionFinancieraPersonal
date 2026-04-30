package com.fabrica.gestionfinancierapersonal.application.repository;

import java.util.*;

import com.fabrica.gestionfinancierapersonal.domain.model.Transaccion;

public interface TransaccionRepository {

    void guardar(Transaccion transaccion);

    Optional<Transaccion> buscarPorId(UUID id);

    List<Transaccion> buscarPorCuentaYUsuario(UUID idCuenta, UUID idUsuario);

    void actualizar(Transaccion transaccion);

    void eliminar(UUID id);

    List<Transaccion> buscarPorCuentaCategoriaYUsuario(UUID cuentaId, UUID categoriaId, UUID usuarioId);
}
