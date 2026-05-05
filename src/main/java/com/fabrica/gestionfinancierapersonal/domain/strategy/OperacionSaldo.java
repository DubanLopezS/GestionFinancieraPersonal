package com.fabrica.gestionfinancierapersonal.domain.strategy;

import com.fabrica.gestionfinancierapersonal.domain.model.Cuenta;

public interface OperacionSaldo {
    void aplicar(Cuenta cuenta, double monto);
}
