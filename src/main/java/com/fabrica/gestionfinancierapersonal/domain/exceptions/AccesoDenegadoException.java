package com.fabrica.gestionfinancierapersonal.domain.exceptions;

public class AccesoDenegadoException extends RuntimeException {

    public AccesoDenegadoException(String mensaje) {
        super(mensaje);
    }
}
