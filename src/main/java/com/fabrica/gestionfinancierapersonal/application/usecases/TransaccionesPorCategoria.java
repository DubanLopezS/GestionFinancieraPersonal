package com.fabrica.gestionfinancierapersonal.application.usecases;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fabrica.gestionfinancierapersonal.application.dtos.MovimientosCategoriaResponse;
import com.fabrica.gestionfinancierapersonal.application.repository.CategoriaRepository;
import com.fabrica.gestionfinancierapersonal.application.repository.CuentaRepository;
import com.fabrica.gestionfinancierapersonal.application.repository.TransaccionRepository;
import com.fabrica.gestionfinancierapersonal.domain.model.Categoria;
import com.fabrica.gestionfinancierapersonal.domain.model.Cuenta;
import com.fabrica.gestionfinancierapersonal.domain.model.Transaccion;

@Service
public class TransaccionesPorCategoria {

    private final TransaccionRepository transaccionRepository;
    private final CategoriaRepository categoriaRepository;
    private final CuentaRepository cuentaRepository;

    public TransaccionesPorCategoria(TransaccionRepository transaccionRepository,
            CategoriaRepository categoriaRepository, CuentaRepository cuentaRepository) {
        this.transaccionRepository = transaccionRepository;
        this.categoriaRepository = categoriaRepository;
        this.cuentaRepository = cuentaRepository;
    }

    public List<MovimientosCategoriaResponse> ejecutar(UUID cuentaId, UUID categoriaId, UUID usuarioId) {


        if (cuentaId == null || categoriaId == null || usuarioId == null) {
            throw new RuntimeException("Los parámetros son obligatorios");
        }

        Cuenta cuenta = cuentaRepository.buscarPorId(cuentaId)
                .orElseThrow(() -> new RuntimeException("La cuenta no existe"));

        if (!cuenta.getUsuario().getIdUsuario().equals(usuarioId)) {
            throw new RuntimeException("La cuenta no le pertenece al usuario");
        }

        Categoria categoria = categoriaRepository.buscarPorId(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no existe"));

        if (categoria.getUsuario() != null &&
                !categoria.getUsuario().getIdUsuario().equals(usuarioId)) {

            throw new RuntimeException("La categoría no pertenece al usuario");
        }

        List<Transaccion> transacciones = transaccionRepository.buscarPorCuentaCategoriaYUsuario(cuentaId, categoriaId,
                usuarioId);

        return transacciones.stream()
                .map(t -> new MovimientosCategoriaResponse(
                        t.getIdTransaccion(),
                        t.getMonto(),
                        t.getTipo().name(),
                        t.getCategoria().getNombre(),
                        t.getFecha()))
                .toList();
    }
}
