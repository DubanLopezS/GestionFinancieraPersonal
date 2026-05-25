package com.fabrica.gestionfinancierapersonal.application.usecases;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.fabrica.gestionfinancierapersonal.application.dtos.CuentaResponse;
import com.fabrica.gestionfinancierapersonal.application.repository.CuentaRepository;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.CampoObligatorioException;

@Service
public class ListarCuentasPorUsuario {

    private final CuentaRepository cuentaRepository;

    public ListarCuentasPorUsuario(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public List<CuentaResponse> ejecutar(UUID idUsuario) {

        if (idUsuario == null) {
            throw new CampoObligatorioException("El ID del usuario es obligatorio");
        }

        if (cuentaRepository.buscarPorUsuario(idUsuario).isEmpty()) {
            throw new IllegalStateException("No se encontraron cuentas para este usuario");
        }

        return cuentaRepository.buscarPorUsuario(idUsuario)
                .stream()
                .map(cuenta -> new CuentaResponse(
                        cuenta.getIdCuenta(),
                        cuenta.getNombre(),
                        cuenta.getSaldo(),
                        cuenta.getMoneda().name()
                ))
                .toList();
    }
}