package com.fabrica.gestionfinancierapersonal.application.exceptions.presupuesto;

public class PresupuestoDuplicadoException extends RuntimeException {

    public PresupuestoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
