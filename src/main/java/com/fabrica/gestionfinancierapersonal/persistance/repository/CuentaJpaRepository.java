package com.fabrica.gestionfinancierapersonal.persistance.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fabrica.gestionfinancierapersonal.domain.model.Cuenta;

@Repository
public interface CuentaJpaRepository extends JpaRepository<Cuenta, UUID> {

    List<Cuenta> findByUsuario_IdUsuario(UUID idUsuario);
}
