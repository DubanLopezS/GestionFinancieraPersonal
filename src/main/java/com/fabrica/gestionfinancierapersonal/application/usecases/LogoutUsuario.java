package com.fabrica.gestionfinancierapersonal.application.usecases;

import org.springframework.stereotype.Service;

@Service
public class LogoutUsuario {

    public void ejecutar() {
        /*
         * En la implementación actual el sistema no mantiene sesiones en el servidor.
         * Por esta razón, no existe una sesión que invalidar al cerrar sesión.
         * Este método se deja como punto de partida para futuras mejoras,
         * como mecanismos de autenticacion y eso...),
         * donde se pueda realizar un logout robusto.
         */
    }
}
