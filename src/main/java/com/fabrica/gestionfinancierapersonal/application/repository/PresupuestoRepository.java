package com.fabrica.gestionfinancierapersonal.application.repository;

import java.util.Optional;
import java.util.UUID;

import com.fabrica.gestionfinancierapersonal.domain.enums.PeriodoPresupuesto;
import com.fabrica.gestionfinancierapersonal.domain.model.Presupuesto;

public interface PresupuestoRepository {

    boolean existeActivoPorUsuarioCategoriaYPeriodo(
            UUID usuarioId,
            UUID categoriaId,
            PeriodoPresupuesto periodo);

    void guardar(Presupuesto presupuesto);

    Optional<Presupuesto> buscarActivoPorUsuarioYCategoria(
            UUID usuarioId,
            UUID categoriaId);
}
