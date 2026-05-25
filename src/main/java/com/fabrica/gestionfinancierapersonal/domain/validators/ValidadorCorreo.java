package com.fabrica.gestionfinancierapersonal.domain.validators;

import org.springframework.stereotype.Component;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.CampoObligatorioException;

@Component
public class ValidadorCorreo {

    private static final String REGEX_CORREO = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    // Valida que el correo tenga formato correcto
    public void validar(String correo) {
        if (correo == null || correo.isBlank()) {
            throw new CampoObligatorioException("El correo no puede estar vacío");
        }

        if (!esValido(correo)) {
            throw new CampoObligatorioException("El formato del correo no es válido");
        }
    }

    // Verifica si el correo es válido 
    public boolean esValido(String correo) {
        return correo != null && correo.matches(REGEX_CORREO);
    }
}
