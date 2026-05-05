package com.fabrica.gestionfinancierapersonal.domain.validators;

import org.springframework.stereotype.Component;

@Component
public class ValidadorTelefono {

    private static final String REGEX_TELEFONO = "\\d{10}";

    // Valida que el teléfono tenga formato correcto
    public void validar(String telefono) {
        if (telefono == null || telefono.isBlank()) {
            throw new IllegalArgumentException("El teléfono no puede estar vacío");
        }

        if (!esValido(telefono)) {
            throw new IllegalArgumentException("El teléfono debe tener exactamente 10 dígitos numéricos");
        }
    }

    // Verifica si el teléfono es válido 
    public boolean esValido(String telefono) {
        return telefono != null && telefono.matches(REGEX_TELEFONO);
    }
}
