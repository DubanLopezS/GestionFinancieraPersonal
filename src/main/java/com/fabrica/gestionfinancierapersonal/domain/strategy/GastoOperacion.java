package com.fabrica.gestionfinancierapersonal.domain.strategy;

import com.fabrica.gestionfinancierapersonal.domain.exceptions.transaccion.SaldoInsuficienteException;
import com.fabrica.gestionfinancierapersonal.domain.model.Cuenta;

public class GastoOperacion implements OperacionSaldo {
    public void aplicar(Cuenta cuenta, double monto) {
        if (cuenta.getSaldo() < monto) {
            throw new SaldoInsuficienteException("Saldo insuficiente");
        }
        cuenta.restarSaldo(monto);
    }
}