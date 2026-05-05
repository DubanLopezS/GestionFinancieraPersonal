package com.fabrica.gestionfinancierapersonal.domain.validators;

import org.springframework.stereotype.Component;

@Component
public class ValidadorNombre {

    private static final String REGEX_NOMBRE = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ' -]{3,15}$";

    // Valida que el nombre tenga formato correcto
    public void validar(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        if (!esValido(nombre)) {
            throw new IllegalArgumentException(
                    "El nombre debe tener entre 3 y 15 caracteres, solo letras, espacios, guiones y apóstrofos");
        }
    }

    // Verifica si el nombre es válido 
    public boolean esValido(String nombre) {
        return nombre != null && nombre.matches(REGEX_NOMBRE);
    }
}
