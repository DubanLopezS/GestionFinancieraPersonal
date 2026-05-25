package com.fabrica.gestionfinancierapersonal.domain.exceptions.usuario;

public class CorreoNoRegistradoException extends RuntimeException {
    public CorreoNoRegistradoException(String mensaje) {
        super(mensaje);
    }
}
