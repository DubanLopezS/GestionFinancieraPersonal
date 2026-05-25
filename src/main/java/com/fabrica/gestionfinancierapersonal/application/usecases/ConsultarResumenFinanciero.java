package com.fabrica.gestionfinancierapersonal.application.usecases;

import java.util.UUID;
import org.springframework.stereotype.Service;
import com.fabrica.gestionfinancierapersonal.application.dtos.ResumenFinancieroResponse;
import com.fabrica.gestionfinancierapersonal.application.repository.TransaccionRepository;
import com.fabrica.gestionfinancierapersonal.application.repository.UsuarioRepository;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.usuario.UsuarioNoEncontradoException;

@Service

public class ConsultarResumenFinanciero {

        private final UsuarioRepository usuarioRepository;
        private final TransaccionRepository transaccionRepository;

        public ConsultarResumenFinanciero(UsuarioRepository usuarioRepository,
                        TransaccionRepository transaccionRepository) {
                this.usuarioRepository = usuarioRepository;
                this.transaccionRepository = transaccionRepository;
        }

        public ResumenFinancieroResponse ejecutar(UUID idUsuario) {

                usuarioRepository.buscarPorId(idUsuario)
                                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

                double ingresos = transaccionRepository
                                .sumarIngresosDelMes(idUsuario);

                double gastos = transaccionRepository
                                .sumarGastosDelMes(idUsuario);

                double balanceNeto = ingresos - gastos;

                return new ResumenFinancieroResponse(
                                ingresos,
                                gastos,
                                balanceNeto);
        }
}
