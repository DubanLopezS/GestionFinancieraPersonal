package com.fabrica.gestionfinancierapersonal.interfaces.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fabrica.gestionfinancierapersonal.application.dtos.ActualizarTransaccionRequest;
import com.fabrica.gestionfinancierapersonal.application.dtos.ActualizarTransaccionResponse;
import com.fabrica.gestionfinancierapersonal.application.dtos.RegistrarTransaccionRequest;
import com.fabrica.gestionfinancierapersonal.application.dtos.RegistrarTransaccionResponse;
import com.fabrica.gestionfinancierapersonal.application.dtos.TransferenciaRequest;
import com.fabrica.gestionfinancierapersonal.application.dtos.TransferenciaResponse;
import com.fabrica.gestionfinancierapersonal.application.usecases.ActualizarTransaccion;
import com.fabrica.gestionfinancierapersonal.application.usecases.RegistrarTransaccion;
import com.fabrica.gestionfinancierapersonal.application.usecases.TransferirDinero;

@RestController
@RequestMapping("/api/transaccion")
public class TransaccionController {

    private final RegistrarTransaccion registrarTransaccion;
    private final ActualizarTransaccion actualizarTransaccion;
    private final TransferirDinero transferirDinero;

    public TransaccionController(RegistrarTransaccion registrarTransaccion,
            ActualizarTransaccion actualizarTransaccion, TransferirDinero transferirDinero) {
        this.registrarTransaccion = registrarTransaccion;
        this.actualizarTransaccion = actualizarTransaccion;
        this.transferirDinero = transferirDinero;
    }

    // Registrar transacción
    @PostMapping("/registrar")
    public ResponseEntity<RegistrarTransaccionResponse> registrar(@RequestBody RegistrarTransaccionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(registrarTransaccion.ejecutar(request));
    }

    @PutMapping("/actualizar")
    public ResponseEntity<ActualizarTransaccionResponse> actualizar(@RequestBody ActualizarTransaccionRequest request) {
        return ResponseEntity.ok(
                actualizarTransaccion.ejecutar(request));
    }

    @PostMapping("/transferencias")
    public ResponseEntity<TransferenciaResponse> transferir(@RequestBody TransferenciaRequest request) {
        return ResponseEntity.ok(
                transferirDinero.ejecutar(request));
    }
}
