package com.fabrica.gestionfinancierapersonal.domain.model;

import java.util.ArrayList;
import java.util.List;
import com.fabrica.gestionfinancierapersonal.domain.enums.TipoCuenta;
import com.fabrica.gestionfinancierapersonal.domain.enums.TipoGasto;

import lombok.Getter;

@Getter
public class Cuenta {

    private String id;
    private String nombre;
    private double saldo;
    private TipoCuenta tipo;
    private List<Transaccion> transacciones;

    public Cuenta(String id, String nombre, TipoCuenta tipo) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Nombre de cuenta obligatorio");
        }

        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.saldo = 0;
        this.transacciones = new ArrayList<>();
    }

    public void agregarTransaccion(Transaccion transaccion) {

        if (transaccion == null) {
            throw new IllegalArgumentException("Transacción inválida");
        }

        if (transaccion.getTipo() == TipoGasto.GASTO &&
                this.saldo < transaccion.getMonto()) {

            throw new IllegalArgumentException("Saldo insuficiente");
        }

        this.transacciones.add(transaccion);

        if (transaccion.getTipo() == TipoGasto.INGRESO) {
            this.saldo += transaccion.getMonto();
        } else {
            this.saldo -= transaccion.getMonto();
        }
    }
}