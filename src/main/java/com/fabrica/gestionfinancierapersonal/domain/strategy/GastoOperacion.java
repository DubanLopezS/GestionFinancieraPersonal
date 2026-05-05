package com.fabrica.gestionfinancierapersonal.domain.strategy;

import com.fabrica.gestionfinancierapersonal.domain.model.Cuenta;

public class GastoOperacion implements OperacionSaldo {
    public void aplicar(Cuenta cuenta, double monto) {
        if (cuenta.getSaldo() < monto) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }
        cuenta.restarSaldo(monto);
    }
}