package com.fabrica.gestionfinancierapersonal.application.usecases;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fabrica.gestionfinancierapersonal.application.dtos.ConsultarPresupuestoResponse;
import com.fabrica.gestionfinancierapersonal.application.exceptions.CategoriaNoEncontradaException;
import com.fabrica.gestionfinancierapersonal.application.exceptions.UsuarioNoEncontradoException;
import com.fabrica.gestionfinancierapersonal.application.exceptions.presupuesto.PresupuestoNoEncontradoException;
import com.fabrica.gestionfinancierapersonal.application.repository.CategoriaRepository;
import com.fabrica.gestionfinancierapersonal.application.repository.PresupuestoRepository;
import com.fabrica.gestionfinancierapersonal.application.repository.UsuarioRepository;
import com.fabrica.gestionfinancierapersonal.domain.model.Categoria;
import com.fabrica.gestionfinancierapersonal.domain.model.Presupuesto;
import com.fabrica.gestionfinancierapersonal.domain.model.Usuario;

@Service
public class ConsultarPresupuesto {

    private final PresupuestoRepository presupuestoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;

    public ConsultarPresupuesto(PresupuestoRepository presupuestoRepository, UsuarioRepository usuarioRepository,
            CategoriaRepository categoriaRepository) {
        this.presupuestoRepository = presupuestoRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public ConsultarPresupuestoResponse ejecutar(UUID idUsuario, UUID idCategoria) {

        Usuario usuario = usuarioRepository.buscarPorId(idUsuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        Categoria categoria = categoriaRepository.buscarPorId(idCategoria)
                .orElseThrow(() -> new CategoriaNoEncontradaException("Categoría no encontrada"));

        if (!categoria.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            throw new CategoriaNoEncontradaException("La categoría no pertenece al usuario");
        }

        Presupuesto presupuesto = presupuestoRepository.buscarActivoPorUsuarioYCategoria(idUsuario, idCategoria)
                .orElseThrow(() -> new PresupuestoNoEncontradoException(
                        "No existe un presupuesto activo para esta categoría"));

        if (presupuesto.estaExpirado()) {
            presupuesto.reiniciarPeriodo();
            presupuestoRepository.guardar(
                    presupuesto);
        }

        // Generar alerta segun el estado del presupuesto
        String alerta = "Estás dentro del presupuesto";
        if (presupuesto.estaExcedido()) {
            alerta = String.format("Has excedido el presupuesto por %.2f", presupuesto.getMontoExcedido());
        } else if (presupuesto.estaCercaDelLimite()) {
            alerta = String.format("Ya has usado el %.2f%% del presupuesto", presupuesto.getPorcentajeUsado());
        }

        return new ConsultarPresupuestoResponse(
                presupuesto.getCategoria().getNombre(),
                presupuesto.getLimite(),
                presupuesto.getMontoGastado(),
                presupuesto.getRestante(),
                presupuesto.getPorcentajeUsado(),
                presupuesto.getPeriodo().name(),
                presupuesto.getActivo(),
                presupuesto.getFechaExpiracion(),
                alerta);
    }
}