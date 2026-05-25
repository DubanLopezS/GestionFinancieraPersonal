package com.fabrica.gestionfinancierapersonal.domain.exceptions.categoria;

public class CategoriaInvalidaParaTransaccionException extends RuntimeException {
    public CategoriaInvalidaParaTransaccionException(String mensaje) {
        super(mensaje);
    }
}
