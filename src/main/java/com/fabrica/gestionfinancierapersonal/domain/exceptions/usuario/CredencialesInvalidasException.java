package com.fabrica.gestionfinancierapersonal.domain.exceptions.usuario;

public class CredencialesInvalidasException extends RuntimeException {
    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }

}
