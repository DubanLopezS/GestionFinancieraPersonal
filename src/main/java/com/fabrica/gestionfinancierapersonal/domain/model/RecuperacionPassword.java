package com.fabrica.gestionfinancierapersonal.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recuperacionPassword")
@Getter
@Setter
@NoArgsConstructor
public class RecuperacionPassword {

    @Id
    @GeneratedValue
    private UUID idRecuperacion;

    private String codigo;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaExpiracion;

    private boolean usado;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

}
