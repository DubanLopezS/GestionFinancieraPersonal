package com.fabrica.gestionfinancierapersonal.persistance.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fabrica.gestionfinancierapersonal.domain.model.Transaccion;

@Repository
public interface TransaccionJpaRepository extends JpaRepository<Transaccion, UUID> {

    List<Transaccion> findByCuenta_IdCuenta(UUID idCuenta);
}
