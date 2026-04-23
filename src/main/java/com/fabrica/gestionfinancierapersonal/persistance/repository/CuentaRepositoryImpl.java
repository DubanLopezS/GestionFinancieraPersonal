package com.fabrica.gestionfinancierapersonal.persistance.repository;

import java.util.*;

import org.springframework.stereotype.Repository;

import com.fabrica.gestionfinancierapersonal.application.repository.CuentaRepository;
import com.fabrica.gestionfinancierapersonal.domain.model.Cuenta;

@Repository
public class CuentaRepositoryImpl implements CuentaRepository {

    private final CuentaJpaRepository jpaRepository;

    public CuentaRepositoryImpl(CuentaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void guardar(Cuenta cuenta) {
        jpaRepository.save(cuenta);
    }

    @Override
    public Optional<Cuenta> buscarPorId(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Cuenta> buscarPorUsuario(UUID idUsuario) {
        return jpaRepository.findByUsuario_IdUsuario(idUsuario);
    }

    @Override
    public void actualizar(Cuenta cuenta) {
        jpaRepository.save(cuenta);
    }

    @Override
    public void eliminar(UUID id) {
        jpaRepository.deleteById(id);
    }
}
