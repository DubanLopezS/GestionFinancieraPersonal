package com.fabrica.gestionfinancierapersonal.domain.exceptions.presupuesto;

public class PresupuestoDuplicadoException extends RuntimeException {

    public PresupuestoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
