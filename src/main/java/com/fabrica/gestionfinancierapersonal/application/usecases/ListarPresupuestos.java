package com.fabrica.gestionfinancierapersonal.application.usecases;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.fabrica.gestionfinancierapersonal.application.dtos.PresupuestoListadoResponse;
import com.fabrica.gestionfinancierapersonal.application.repository.PresupuestoRepository;
import com.fabrica.gestionfinancierapersonal.application.repository.UsuarioRepository;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.usuario.UsuarioNoEncontradoException;
import com.fabrica.gestionfinancierapersonal.domain.model.Presupuesto;

@Service
public class ListarPresupuestos {

        private final PresupuestoRepository presupuestoRepository;
        private final UsuarioRepository usuarioRepository;

        public ListarPresupuestos(
                        PresupuestoRepository presupuestoRepository, UsuarioRepository usuarioRepository) {
                this.presupuestoRepository = presupuestoRepository;
                this.usuarioRepository = usuarioRepository;
        }

        public List<PresupuestoListadoResponse> ejecutar(UUID idUsuario) {

                usuarioRepository.buscarPorId(idUsuario)
                                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

                List<Presupuesto> presupuestos = presupuestoRepository
                                .listarActivosPorUsuario(idUsuario);

                // Reiniciar ciclos expirados
                presupuestos.forEach(presupuesto -> {
                        if (presupuesto.estaExpirado()) {
                                presupuesto.reiniciarPeriodo();
                                presupuestoRepository.guardar(
                                                presupuesto);
                        }
                });

                return presupuestos.stream()

                                // Ordenar por periodo y categoria
                                .sorted(Comparator
                                                .comparing(
                                                                (Presupuesto p) -> p.getPeriodo().name())
                                                .thenComparing(
                                                                p -> p.getCategoria().getNombre()))

                                // Mapear response
                                .map(presupuesto -> {
                                        String alerta = generarAlerta(
                                                        presupuesto);
                                        return new PresupuestoListadoResponse(
                                                        presupuesto.getCategoria()
                                                                        .getNombre(),
                                                        presupuesto.getLimite(),
                                                        presupuesto.getPeriodo()
                                                                        .name(),
                                                        presupuesto.getMontoGastado(),
                                                        presupuesto.getPorcentajeUsado(),
                                                        presupuesto.getRestante(),
                                                        alerta);
                                })
                                .toList();
        }

        private String generarAlerta(
                        Presupuesto presupuesto) {
                if (presupuesto.estaExcedido()) {
                        return String.format(
                                        "Has excedido el presupuesto por %.2f",
                                        presupuesto.getMontoExcedido());
                }
                if (presupuesto.estaCercaDelLimite()) {
                        return String.format(
                                        "Ya has usado el %.2f%% del presupuesto",
                                        presupuesto.getPorcentajeUsado());
                }
                return "Estás dentro del presupuesto";
        }
}