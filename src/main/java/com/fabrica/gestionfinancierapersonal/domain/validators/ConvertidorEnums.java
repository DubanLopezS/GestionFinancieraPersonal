package com.fabrica.gestionfinancierapersonal.domain.validators;

import org.springframework.stereotype.Component;

import com.fabrica.gestionfinancierapersonal.domain.enums.Moneda;
import com.fabrica.gestionfinancierapersonal.domain.enums.Periodicidad;
import com.fabrica.gestionfinancierapersonal.domain.enums.TipoCuenta;
import com.fabrica.gestionfinancierapersonal.domain.enums.TipoTransaccion;


@Component
public class ConvertidorEnums {
    
    // Convierte un String a TipoCuenta
    public TipoCuenta convertirATipoCuenta(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("El tipo de cuenta no puede estar vacío");
        }

        try {
            return TipoCuenta.valueOf(tipo.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Tipo de cuenta inválido. Valores válidos: " + obtenerValoresTipoCuenta());
        }
    }


    // Convierte un String a Moneda
    public Moneda convertirAMoneda(String moneda) {
        if (moneda == null || moneda.isBlank()) {
            throw new IllegalArgumentException("La moneda no puede estar vacía");
        }

        try {
            return Moneda.valueOf(moneda.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Moneda inválida. Valores válidos: " + obtenerValoresMoneda());
        }
    }

    // Convierte un String a TipoTransaccion
    public TipoTransaccion convertirATipoTransaccion(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("El tipo de transacción no puede estar vacío");
        }

        try {
            return TipoTransaccion.valueOf(tipo.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Tipo de transacción inválido. Valores válidos: " + obtenerValoresTipoTransaccion());
        }
    }

    // Convierte un String a Periodicidad
    public Periodicidad convertirAPeriodicidad(String periodicidad) {
        if (periodicidad == null || periodicidad.isBlank()) {
            throw new IllegalArgumentException("La periodicidad no puede estar vacía");
        }

        try {
            return Periodicidad.valueOf(periodicidad.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Periodicidad inválida. Valores válidos: " + obtenerValoresPeriodicidad());
        }
    }


    private String obtenerValoresTipoCuenta() {
        return String.join(", ", 
            org.apache.commons.lang3.EnumUtils.getEnumList(TipoCuenta.class).stream()
                .map(Enum::name)
                .toArray(String[]::new));
    }

    private String obtenerValoresMoneda() {
        return String.join(", ",
            org.apache.commons.lang3.EnumUtils.getEnumList(Moneda.class).stream()
                .map(Enum::name)
                .toArray(String[]::new));
    }

    private String obtenerValoresTipoTransaccion() {
        return String.join(", ",
            org.apache.commons.lang3.EnumUtils.getEnumList(TipoTransaccion.class).stream()
                .map(Enum::name)
                .toArray(String[]::new));
    }

    private String obtenerValoresPeriodicidad() {
        return String.join(", ",
            org.apache.commons.lang3.EnumUtils.getEnumList(Periodicidad.class).stream()
                .map(Enum::name)
                .toArray(String[]::new));
    }
}
