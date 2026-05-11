package com.fabrica.gestionfinancierapersonal.persistance.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fabrica.gestionfinancierapersonal.domain.model.RecuperacionPassword;

@Repository
public interface RecuperacionPasswordJpaRepository extends JpaRepository<RecuperacionPassword, UUID> {

    Optional<RecuperacionPassword>findByCodigo(String codigo);
}