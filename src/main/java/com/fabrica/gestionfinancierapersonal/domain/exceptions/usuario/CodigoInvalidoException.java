package com.fabrica.gestionfinancierapersonal.domain.exceptions.usuario;

public class CodigoInvalidoException extends RuntimeException {
    public CodigoInvalidoException(String mensaje) {
        super(mensaje);
    }

}
