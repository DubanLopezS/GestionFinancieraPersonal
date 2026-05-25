package com.fabrica.gestionfinancierapersonal.domain.exceptions.presupuesto;

public class LimitePresupuestoInvalidoException extends RuntimeException {

    public LimitePresupuestoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
