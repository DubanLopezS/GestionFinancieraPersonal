package com.fabrica.gestionfinancierapersonal.domain.exceptions.transaccion;

public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException(String mensaje) {
        super(mensaje);
    }

}
