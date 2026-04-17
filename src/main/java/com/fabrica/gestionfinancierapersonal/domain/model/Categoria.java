package com.fabrica.gestionfinancierapersonal.domain.model;

import java.util.UUID;

import com.fabrica.gestionfinancierapersonal.domain.enums.TipoTransaccion;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

    private String icono;
    private String color;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;


    protected Categoria() {
    }

    public Categoria(String nombre, TipoTransaccion tipo, String icono, String color, Usuario usuario) {
        this.idCategoria = UUID.randomUUID();
        this.nombre = nombre;
        this.tipo = tipo;
        this.icono = icono;
        this.color = color;
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
        if (icono == null || icono.isBlank()) {
            throw new IllegalArgumentException("El icono es obligatorio");
        }
        if (color == null || color.isBlank()) {
            throw new IllegalArgumentException("El color es obligatorio");
        }
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }
    }
}
