package com.fabrica.gestionfinancierapersonal.domain.exceptions.usuario;

public class ContrasenasNoCoincidenException extends RuntimeException {
    public ContrasenasNoCoincidenException(String message) {
        super(message);
    }

}
