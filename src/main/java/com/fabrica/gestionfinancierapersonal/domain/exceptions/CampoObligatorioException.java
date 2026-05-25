package com.fabrica.gestionfinancierapersonal.domain.exceptions;

public class CampoObligatorioException extends RuntimeException {
    public CampoObligatorioException(String mensaje) {
        super(mensaje);
    }

}
