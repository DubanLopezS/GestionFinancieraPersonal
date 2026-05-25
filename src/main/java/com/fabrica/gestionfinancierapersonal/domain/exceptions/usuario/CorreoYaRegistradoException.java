package com.fabrica.gestionfinancierapersonal.domain.exceptions.usuario;

public class CorreoYaRegistradoException extends RuntimeException {

    public CorreoYaRegistradoException(String mensaje) {
        super(mensaje);
    }
}
