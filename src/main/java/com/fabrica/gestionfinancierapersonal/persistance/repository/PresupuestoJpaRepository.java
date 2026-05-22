package com.fabrica.gestionfinancierapersonal.persistance.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fabrica.gestionfinancierapersonal.domain.enums.PeriodoPresupuesto;
import com.fabrica.gestionfinancierapersonal.domain.model.Presupuesto;

@Repository
public interface PresupuestoJpaRepository extends JpaRepository<Presupuesto, UUID> {

        boolean existsByUsuario_IdUsuarioAndCategoria_IdCategoriaAndPeriodoAndActivoTrue(
                        UUID usuarioId,
                        UUID categoriaId,
                        PeriodoPresupuesto periodo);

        Optional<Presupuesto> findByUsuario_IdUsuarioAndCategoria_IdCategoriaAndActivoTrue(
                        UUID usuarioId,
                        UUID categoriaId);

        List<Presupuesto> findByUsuario_IdUsuarioAndActivoTrue(
                        UUID usuarioId);
}