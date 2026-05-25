package com.fabrica.gestionfinancierapersonal.domain.exceptions.transaccion;

public class TransaccionNoExistenteException extends RuntimeException {
    public TransaccionNoExistenteException(String mensaje) {
        super(mensaje);
    }

}
