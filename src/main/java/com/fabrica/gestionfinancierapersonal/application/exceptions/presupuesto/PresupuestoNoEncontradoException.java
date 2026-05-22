package com.fabrica.gestionfinancierapersonal.application.exceptions.presupuesto;

public class PresupuestoNoEncontradoException extends RuntimeException {

    public PresupuestoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
