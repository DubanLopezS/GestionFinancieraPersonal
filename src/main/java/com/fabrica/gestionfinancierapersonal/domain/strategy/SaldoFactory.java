package com.fabrica.gestionfinancierapersonal.domain.strategy;

import com.fabrica.gestionfinancierapersonal.domain.enums.TipoTransaccion;

public class SaldoFactory {

    public static OperacionSaldo crear(TipoTransaccion tipo) {
        return switch (tipo) {
            case INGRESO -> new IngresoOperacion();
            case GASTO -> new GastoOperacion();
        };
    }
}