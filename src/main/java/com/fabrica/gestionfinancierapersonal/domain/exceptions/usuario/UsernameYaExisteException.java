package com.fabrica.gestionfinancierapersonal.domain.exceptions.usuario;

public class UsernameYaExisteException extends RuntimeException {
    public UsernameYaExisteException(String message) {
        super(message);
    }

}
