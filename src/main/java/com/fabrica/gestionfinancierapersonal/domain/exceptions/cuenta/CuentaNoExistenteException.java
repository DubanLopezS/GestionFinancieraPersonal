package com.fabrica.gestionfinancierapersonal.domain.exceptions.cuenta;

public class CuentaNoExistenteException extends RuntimeException {
    public CuentaNoExistenteException(String mensaje) {
        super(mensaje);
    }

}
