package com.fabrica.gestionfinancierapersonal.domain.model;

import lombok.Getter;
import java.time.LocalDate;

import com.fabrica.gestionfinancierapersonal.domain.enums.TipoGasto;

@Getter
public class Transaccion {

    private String id;
    private double monto;
    private TipoGasto tipo;
    private LocalDate fecha;

    public Transaccion(String id, double monto, TipoGasto tipo, LocalDate fecha) {

        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }

        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de transacción obligatorio");
        }

        this.id = id;
        this.monto = monto;
        this.tipo = tipo;
        this.fecha = fecha != null ? fecha : LocalDate.now();
    }
}
