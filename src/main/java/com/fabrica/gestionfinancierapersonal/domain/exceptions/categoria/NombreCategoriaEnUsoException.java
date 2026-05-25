package com.fabrica.gestionfinancierapersonal.domain.exceptions.categoria;

public class NombreCategoriaEnUsoException extends RuntimeException {
    public NombreCategoriaEnUsoException(String mensaje) {
        super(mensaje);
    }

}
