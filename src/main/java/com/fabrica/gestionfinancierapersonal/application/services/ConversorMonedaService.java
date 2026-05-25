package com.fabrica.gestionfinancierapersonal.application.services;

import com.fabrica.gestionfinancierapersonal.application.dtos.ConversionMonedaResponse;
import com.fabrica.gestionfinancierapersonal.domain.enums.Moneda;

public interface ConversorMonedaService {

    ConversionMonedaResponse convertir(
            double monto,
            Moneda monedaOrigen,
            Moneda monedaDestino);
}