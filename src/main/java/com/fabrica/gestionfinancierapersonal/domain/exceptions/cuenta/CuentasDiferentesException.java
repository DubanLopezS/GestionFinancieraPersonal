package com.fabrica.gestionfinancierapersonal.domain.exceptions.cuenta;

public class CuentasDiferentesException extends RuntimeException {
    public CuentasDiferentesException(String mensaje) {
        super(mensaje);
    }

}
