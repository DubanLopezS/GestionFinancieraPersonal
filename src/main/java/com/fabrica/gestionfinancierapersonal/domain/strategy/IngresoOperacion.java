package com.fabrica.gestionfinancierapersonal.domain.strategy;

import com.fabrica.gestionfinancierapersonal.domain.model.Cuenta;

public class IngresoOperacion implements OperacionSaldo {
    public void aplicar(Cuenta cuenta, double monto) {
        cuenta.sumarSaldo(monto);
    }
}
