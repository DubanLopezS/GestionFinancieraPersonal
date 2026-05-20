package com.fabrica.gestionfinancierapersonal.domain.enums;

import java.time.LocalDateTime;

public enum PeriodoPresupuesto {

    DIARIO {
        @Override
        public LocalDateTime calcularExpiracion(
                LocalDateTime fecha) {

            return fecha.plusDays(1);
        }
    },

    SEMANAL {
        @Override
        public LocalDateTime calcularExpiracion(
                LocalDateTime fecha) {

            return fecha.plusWeeks(1);
        }
    },

    MENSUAL {
        @Override
        public LocalDateTime calcularExpiracion(
                LocalDateTime fecha) {

            return fecha.plusMonths(1);
        }
    },

    ANUAL {
        @Override
        public LocalDateTime calcularExpiracion(
                LocalDateTime fecha) {

            return fecha.plusYears(1);
        }
    };

    public abstract LocalDateTime calcularExpiracion(
            LocalDateTime fecha);
}
