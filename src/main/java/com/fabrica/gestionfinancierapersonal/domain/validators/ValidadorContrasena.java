package com.fabrica.gestionfinancierapersonal.domain.validators;

import org.springframework.stereotype.Component;

@Component
public class ValidadorContrasena {

    //Valida que la contraseña cumpla criterios de seguridad
    public void validar(String contrasena) {
        if (contrasena == null || contrasena.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }

        if (!esValida(contrasena)) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener: mínimo 8 caracteres, mayúscula, minúscula, número y carácter especial");
        }
    }

    
    // Verifica si la contraseña es válida 
    public boolean esValida(String contrasena) {
        if (contrasena == null || contrasena.length() < 8) {
            return false;
        }

        boolean tieneMayuscula = contrasena.matches(".*[A-Z].*");
        boolean tieneMinuscula = contrasena.matches(".*[a-z].*");
        boolean tieneNumero = contrasena.matches(".*\\d.*");
        boolean tieneEspecial = contrasena.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?].*");

        return tieneMayuscula && tieneMinuscula && tieneNumero && tieneEspecial;
    }
}
