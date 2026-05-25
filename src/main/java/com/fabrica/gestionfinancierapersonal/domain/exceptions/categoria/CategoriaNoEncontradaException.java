package com.fabrica.gestionfinancierapersonal.domain.exceptions.categoria;

public class CategoriaNoEncontradaException extends RuntimeException {

    public CategoriaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
