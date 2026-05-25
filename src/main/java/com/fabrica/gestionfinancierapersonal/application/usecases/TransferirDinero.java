package com.fabrica.gestionfinancierapersonal.application.usecases;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fabrica.gestionfinancierapersonal.application.dtos.ConversionMonedaResponse;
import com.fabrica.gestionfinancierapersonal.application.dtos.TransferenciaRequest;
import com.fabrica.gestionfinancierapersonal.application.dtos.TransferenciaResponse;
import com.fabrica.gestionfinancierapersonal.application.repository.CategoriaRepository;
import com.fabrica.gestionfinancierapersonal.application.repository.CuentaRepository;
import com.fabrica.gestionfinancierapersonal.application.repository.TransaccionRepository;
import com.fabrica.gestionfinancierapersonal.application.services.ConversorMonedaService;
import com.fabrica.gestionfinancierapersonal.domain.enums.Periodicidad;
import com.fabrica.gestionfinancierapersonal.domain.enums.TipoTransaccion;
import com.fabrica.gestionfinancierapersonal.domain.model.Categoria;
import com.fabrica.gestionfinancierapersonal.domain.model.Cuenta;
import com.fabrica.gestionfinancierapersonal.domain.model.Transaccion;

import jakarta.transaction.Transactional;

@Service
public class TransferirDinero {

        private final CategoriaRepository categoriaRepository;
        private final CuentaRepository cuentaRepository;
        private final TransaccionRepository transaccionRepository;
        private final ConversorMonedaService conversorMonedaService;

        public TransferirDinero(CategoriaRepository categoriaRepository, CuentaRepository cuentaRepository,
                        TransaccionRepository transaccionRepository, ConversorMonedaService conversorMonedaService) {
                this.categoriaRepository = categoriaRepository;
                this.cuentaRepository = cuentaRepository;
                this.transaccionRepository = transaccionRepository;
                this.conversorMonedaService = conversorMonedaService;
        }

        @Transactional
        public TransferenciaResponse ejecutar(TransferenciaRequest request) {

                Cuenta origen = cuentaRepository
                                .buscarPorIdYUsuario(request.idCuentaOrigen(), request.idUsuario())
                                .orElseThrow(() -> new RuntimeException("Cuenta origen no encontrada"));

                Cuenta destino = cuentaRepository
                                .buscarPorIdYUsuario(request.idCuentaDestino(), request.idUsuario())
                                .orElseThrow(() -> new RuntimeException("Cuenta destino no encontrada"));

                if (origen.getIdCuenta().equals(destino.getIdCuenta())) {
                        throw new RuntimeException("Las cuentas deben ser diferentes");
                }

                if (origen.getSaldo() < request.monto()) {
                        throw new RuntimeException("Saldo insuficiente");
                }

                ConversionMonedaResponse conversion = conversorMonedaService.convertir(
                                request.monto(),
                                origen.getMoneda(),
                                destino.getMoneda());

                double montoDestino = conversion.montoConvertido();
                Categoria salida = categoriaRepository
                                .buscarPorNombreYEsSistemaTrue("Transferencia_Salida")
                                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

                Categoria entrada = categoriaRepository
                                .buscarPorNombreYEsSistemaTrue("Transferencia_Entrada")
                                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

                UUID transferenciaId = UUID.randomUUID();

                // Transacción salida
                Transaccion t1 = new Transaccion(
                                request.monto(),
                                TipoTransaccion.GASTO,
                                Periodicidad.OCASIONAL,
                                origen,
                                salida,
                                transferenciaId);

                // Transacción entrada
                Transaccion t2 = new Transaccion(
                                montoDestino,
                                TipoTransaccion.INGRESO,
                                Periodicidad.OCASIONAL,
                                destino,
                                entrada,
                                transferenciaId);

                // Actualizar saldos
                origen.setSaldo(origen.getSaldo() - request.monto());
                destino.setSaldo(destino.getSaldo() + montoDestino);

                cuentaRepository.actualizar(origen);
                cuentaRepository.actualizar(destino);

                transaccionRepository.guardar(t1);
                transaccionRepository.guardar(t2);

                return new TransferenciaResponse(
                                transferenciaId,
                                origen.getIdCuenta(),
                                destino.getIdCuenta(),
                                conversion.trm());
        }
}
