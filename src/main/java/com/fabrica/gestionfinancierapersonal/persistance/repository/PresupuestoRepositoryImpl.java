package com.fabrica.gestionfinancierapersonal.persistance.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.fabrica.gestionfinancierapersonal.application.repository.PresupuestoRepository;
import com.fabrica.gestionfinancierapersonal.domain.enums.PeriodoPresupuesto;
import com.fabrica.gestionfinancierapersonal.domain.model.Presupuesto;

@Repository
public class PresupuestoRepositoryImpl implements PresupuestoRepository {

        private final PresupuestoJpaRepository presupuestoJpaRepository;

        public PresupuestoRepositoryImpl(
                        PresupuestoJpaRepository presupuestoJpaRepository) {
                this.presupuestoJpaRepository = presupuestoJpaRepository;
        }

        @Override
        public boolean existeActivoPorUsuarioCategoriaYPeriodo(
                        UUID usuarioId,
                        UUID categoriaId,
                        PeriodoPresupuesto periodo) {
                return presupuestoJpaRepository
                                .existsByUsuario_IdUsuarioAndCategoria_IdCategoriaAndPeriodoAndActivoTrue(
                                                usuarioId,
                                                categoriaId,
                                                periodo);
        }

        @Override
        public void guardar(
                        Presupuesto presupuesto) {
                presupuestoJpaRepository
                                .save(presupuesto);
        }

        @Override
        public Optional<Presupuesto> buscarActivoPorUsuarioYCategoria(
                        UUID usuarioId,
                        UUID categoriaId) {
                return presupuestoJpaRepository
                                .findByUsuario_IdUsuarioAndCategoria_IdCategoriaAndActivoTrue(
                                                usuarioId,
                                                categoriaId);
        }

        @Override
        public List<Presupuesto> listarActivosPorUsuario(
                        UUID usuarioId) {
                return presupuestoJpaRepository
                                .findByUsuario_IdUsuarioAndActivoTrue(
                                                usuarioId);
        }
}