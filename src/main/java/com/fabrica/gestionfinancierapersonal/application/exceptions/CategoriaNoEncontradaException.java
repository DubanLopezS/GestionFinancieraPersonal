package com.fabrica.gestionfinancierapersonal.application.exceptions;

public class CategoriaNoEncontradaException extends RuntimeException {

    public CategoriaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
