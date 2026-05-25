package com.fabrica.gestionfinancierapersonal.domain.exceptions.presupuesto;

public class PresupuestoNoEncontradoException extends RuntimeException {

    public PresupuestoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
