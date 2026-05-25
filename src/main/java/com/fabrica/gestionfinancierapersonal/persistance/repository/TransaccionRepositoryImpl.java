package com.fabrica.gestionfinancierapersonal.persistance.repository;

import java.util.*;

import org.springframework.stereotype.Repository;

import com.fabrica.gestionfinancierapersonal.application.repository.TransaccionRepository;
import com.fabrica.gestionfinancierapersonal.domain.model.Categoria;
import com.fabrica.gestionfinancierapersonal.domain.model.Transaccion;

@Repository
public class TransaccionRepositoryImpl implements TransaccionRepository {

    private final TransaccionJpaRepository jpaRepository;

    public TransaccionRepositoryImpl(TransaccionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void guardar(Transaccion transaccion) {
        jpaRepository.save(transaccion);
    }

    @Override
    public Optional<Transaccion> buscarPorId(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Transaccion> buscarPorCuentaYUsuario(UUID cuentaId, UUID usuarioId) {
        return jpaRepository.findByCuentaYUsuario(cuentaId, usuarioId);
    }

    @Override
    public void actualizar(Transaccion transaccion) {
        jpaRepository.save(transaccion);
    }

    @Override
    public void eliminar(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Transaccion> buscarPorCuentaCategoriaYUsuario(
            UUID cuentaId,
            UUID categoriaId,
            UUID usuarioId) {
        return jpaRepository.findByCuentaCategoriaYUsuario(
                cuentaId, categoriaId, usuarioId);
    }

    @Override
    public double sumarIngresosDelMes(UUID usuarioId) {
        return jpaRepository.sumarIngresosDelMes(usuarioId);
    }

    @Override
    public double sumarGastosDelMes(UUID usuarioId) {
        return jpaRepository.sumarGastosDelMes(usuarioId);
    }

    @Override
    public List<Categoria> obtenerCategoriaMayorGasto(UUID usuarioId) {
        return jpaRepository.obtenerCategoriaMayorGasto(usuarioId);
    }

    @Override
    public double sumarGastosPorCategoriaDelMes(UUID usuarioId, UUID categoriaId) {
        return jpaRepository.sumarGastosPorCategoriaDelMes(usuarioId, categoriaId);
    }

    @Override
    public List<Transaccion> obtenerGastosPorCategoria(UUID usuarioId, UUID categoriaId) {
        return jpaRepository.obtenerGastosPorCategoria(usuarioId, categoriaId);
    }

    @Override
    public double sumarGastosPorMes(UUID usuarioId, int mes, int anio) {
        return jpaRepository.sumarGastosPorMes(usuarioId, mes, anio);
    }
}
