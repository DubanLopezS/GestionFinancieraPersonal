package com.fabrica.gestionfinancierapersonal.domain.model;

import lombok.Getter;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fabrica.gestionfinancierapersonal.domain.enums.Periodicidad;
import com.fabrica.gestionfinancierapersonal.domain.enums.TipoTransaccion;

@Getter
public class Transaccion {

    private UUID idTransaccion;
    private double monto;
    private TipoTransaccion tipo;
    private Periodicidad periodicidad;
    private LocalDateTime fecha;

    public Transaccion(double monto, TipoTransaccion tipo, Periodicidad periodicidad) {

        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }

        if (tipo == null) {
            throw new IllegalArgumentException("El Tipo es obligatorio");
        }

        if (periodicidad == null) {
            throw new IllegalArgumentException("La Periodicidad es obligatoria");
        }

        this.idTransaccion = UUID.randomUUID();
        this.monto = monto;
        this.tipo = tipo;
        this.periodicidad = periodicidad;
        this.fecha = LocalDateTime.now();
    }
}
