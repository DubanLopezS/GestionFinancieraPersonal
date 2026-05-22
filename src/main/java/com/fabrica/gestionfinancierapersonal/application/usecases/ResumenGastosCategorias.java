package com.fabrica.gestionfinancierapersonal.application.usecases;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fabrica.gestionfinancierapersonal.application.dtos.ResumenCategoriasResponse;
import com.fabrica.gestionfinancierapersonal.application.repository.CuentaRepository;
import com.fabrica.gestionfinancierapersonal.application.repository.TransaccionRepository;
import com.fabrica.gestionfinancierapersonal.domain.enums.TipoTransaccion;
import com.fabrica.gestionfinancierapersonal.domain.model.Cuenta;
import com.fabrica.gestionfinancierapersonal.domain.model.Transaccion;

@Service
public class ResumenGastosCategorias {

    private final TransaccionRepository transaccionRepository;
    private final CuentaRepository cuentaRepository;

    public ResumenGastosCategorias(TransaccionRepository transaccionRepository, CuentaRepository cuentaRepository) {
        this.transaccionRepository = transaccionRepository;
        this.cuentaRepository = cuentaRepository;
    }

    public List<ResumenCategoriasResponse> ejecutar(UUID idCuenta, UUID idUsuario) {

        
        if (idCuenta == null || idUsuario == null) {
            throw new RuntimeException("Los parámetros son obligatorios");
        }

        Cuenta cuenta = cuentaRepository.buscarPorId(idCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no existe"));

        if (!cuenta.getUsuario().getIdUsuario().equals(idUsuario)) {
            throw new RuntimeException("La cuenta no pertenece al usuario");
        }

        List<Transaccion> transacciones = transaccionRepository.buscarPorCuentaYUsuario(idCuenta, idUsuario);

        // Filtrar solo GASTOS
        List<Transaccion> gastos = transacciones.stream()
                .filter(t -> t.getTipo() == TipoTransaccion.GASTO)
                .toList();

        if (gastos.isEmpty()) {
            return List.of();
        }

        Map<String, BigDecimal> totalesPorCategoria = new HashMap<>();

        for (Transaccion t : gastos) {
            String nombreCategoria = t.getCategoria().getNombre();

            totalesPorCategoria.merge(
                    nombreCategoria,
                    BigDecimal.valueOf(t.getMonto()),
                    BigDecimal::add);
        }

        // Calcular total general
        BigDecimal totalGeneral = totalesPorCategoria.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<ResumenCategoriasResponse> resultado = new ArrayList<>();

        for (Map.Entry<String, BigDecimal> entry : totalesPorCategoria.entrySet()) {

            BigDecimal totalCategoria = entry.getValue();

            double porcentaje = totalCategoria
                    .divide(totalGeneral, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue();

            porcentaje = Math.round(porcentaje * 100.0) / 100.0;

            resultado.add(new ResumenCategoriasResponse(
                    entry.getKey(),
                    totalCategoria.doubleValue(),
                    porcentaje));
        }

        resultado.sort((a, b) -> Double.compare(b.total(), a.total()));

        return resultado;
    }
}
