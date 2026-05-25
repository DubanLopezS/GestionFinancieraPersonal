package com.fabrica.gestionfinancierapersonal.domain.exceptions.usuario;

public class UsuarioNoEncontradoException extends RuntimeException {

    public UsuarioNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
