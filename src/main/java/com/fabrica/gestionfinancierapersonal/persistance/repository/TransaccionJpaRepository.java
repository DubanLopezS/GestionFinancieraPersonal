package com.fabrica.gestionfinancierapersonal.persistance.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fabrica.gestionfinancierapersonal.domain.model.Transaccion;

@Repository
public interface TransaccionJpaRepository extends JpaRepository<Transaccion, UUID> {

    @Query("""
                SELECT t FROM Transaccion t
                JOIN FETCH t.categoria
                WHERE t.cuenta.idCuenta = :idCuenta
            """)
    List<Transaccion> findByCuentaConCategoria(UUID idCuenta);
}
