package com.fabrica.gestionfinancierapersonal.domain.exceptions.categoria;

public class CategoriaNoExistenteException extends RuntimeException {
    public CategoriaNoExistenteException(String mensaje) {
        super(mensaje);
    }

}
