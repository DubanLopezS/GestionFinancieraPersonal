package com.fabrica.gestionfinancierapersonal.application.exceptions;

public class AccesoDenegadoException extends RuntimeException {

    public AccesoDenegadoException(String mensaje) {
        super(mensaje);
    }
}
