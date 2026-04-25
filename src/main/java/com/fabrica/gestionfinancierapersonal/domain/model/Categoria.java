package com.fabrica.gestionfinancierapersonal.domain.model;

import java.util.UUID;

import com.fabrica.gestionfinancierapersonal.domain.enums.TipoTransaccion;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "categorias")
@Getter
public class Categoria {

    @Id
    private UUID idCategoria;

    private String nombre;

    @Enumerated(EnumType.STRING)
    private TipoTransaccion tipo;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "es_sistema")
    private boolean esSistema;

    protected Categoria() {
    }

    public Categoria(String nombre, TipoTransaccion tipo, Usuario usuario) {
        this.idCategoria = UUID.randomUUID();
        this.nombre = nombre;
        this.tipo = tipo;
        this.usuario = usuario;

        validar();
    }

    private void validar() {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo es obligatorio");
        }
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }
    }
}
