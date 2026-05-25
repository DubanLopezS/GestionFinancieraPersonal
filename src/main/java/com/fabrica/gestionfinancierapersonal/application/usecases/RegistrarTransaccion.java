package com.fabrica.gestionfinancierapersonal.application.usecases;

import java.util.Optional;
import org.springframework.stereotype.Service;
import com.fabrica.gestionfinancierapersonal.application.dtos.RegistrarTransaccionRequest;
import com.fabrica.gestionfinancierapersonal.application.dtos.RegistrarTransaccionResponse;
import com.fabrica.gestionfinancierapersonal.application.repository.CategoriaRepository;
import com.fabrica.gestionfinancierapersonal.application.repository.PresupuestoRepository;
import com.fabrica.gestionfinancierapersonal.application.repository.UsuarioRepository;
import com.fabrica.gestionfinancierapersonal.domain.enums.Periodicidad;
import com.fabrica.gestionfinancierapersonal.domain.enums.TipoTransaccion;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.CampoObligatorioException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.MontoMayorException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.categoria.CategoriaInvalidaParaTransaccionException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.categoria.CategoriaNoEncontradaException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.usuario.UsuarioNoEncontradoException;
import com.fabrica.gestionfinancierapersonal.domain.model.Categoria;
import com.fabrica.gestionfinancierapersonal.domain.model.Cuenta;
import com.fabrica.gestionfinancierapersonal.domain.model.Presupuesto;
import com.fabrica.gestionfinancierapersonal.domain.model.Transaccion;
import com.fabrica.gestionfinancierapersonal.domain.model.Usuario;
import com.fabrica.gestionfinancierapersonal.domain.validators.ConvertidorEnums;

@Service
public class RegistrarTransaccion {

    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final ConvertidorEnums convertidorEnums;
    private final PresupuestoRepository presupuestoRepository;

    public RegistrarTransaccion(UsuarioRepository usuarioRepository, CategoriaRepository categoriaRepository,
            ConvertidorEnums convertidorEnums, PresupuestoRepository presupuestoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.convertidorEnums = convertidorEnums;
        this.presupuestoRepository = presupuestoRepository;

    }

    public RegistrarTransaccionResponse ejecutar(RegistrarTransaccionRequest request) {

        // Validaciones
        if (request.idUsuario() == null) {
            throw new CampoObligatorioException("El id del usuario es obligatorio");
        }

        if (request.idCuenta() == null) {
            throw new CampoObligatorioException("El id de la cuenta es obligatorio");
        }

        if (request.monto() <= 0) {
            throw new MontoMayorException("El monto debe ser mayor a 0");
        }

        if (request.tipoTransaccion() == null || request.tipoTransaccion().isBlank()) {
            throw new CampoObligatorioException("El tipo es obligatorio");
        }

        if (request.periodicidad() == null || request.periodicidad().isBlank()) {
            throw new CampoObligatorioException("La periodicidad es obligatoria");
        }

        if (request.idCategoria() == null) {
            throw new CampoObligatorioException("La categoría es obligatoria");
        }

        // Convertir STRING → ENUM
        TipoTransaccion tipo = convertidorEnums.convertirATipoTransaccion(request.tipoTransaccion());

        Periodicidad periodicidad = convertidorEnums.convertirAPeriodicidad(request.periodicidad());

        // Buscar usuario
        Usuario usuario = usuarioRepository.buscarPorId(request.idUsuario())
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        // Buscar cuenta
        Cuenta cuenta = usuario.buscarCuentaPorId(request.idCuenta());

        Categoria categoria = categoriaRepository
                .buscarPorIdYUsuario(request.idCategoria(), request.idUsuario())
                .orElseThrow(() -> new CategoriaNoEncontradaException("Categoría no encontrada"));

        TipoTransaccion tipoTransaccion = TipoTransaccion.valueOf(request.tipoTransaccion().toUpperCase());

        if (!categoria.getTipo().equals(tipoTransaccion)) {
            throw new CategoriaInvalidaParaTransaccionException("La categoría no coincide con el tipo de transacción");
        }

        // Crear transacción
        Transaccion transaccion = new Transaccion(
                request.monto(),
                tipo,
                periodicidad,
                cuenta,
                categoria);

                
        // Validar presupuesto solo para gastos
        String alerta = null;
        if (tipo == TipoTransaccion.GASTO
                && !categoria.isEsSistema()) {
            Optional<Presupuesto> presupuestoOptional = presupuestoRepository
                    .buscarActivoPorUsuarioYCategoria(
                            usuario.getIdUsuario(),
                            categoria.getIdCategoria());
            if (presupuestoOptional.isPresent()) {
                Presupuesto presupuesto = presupuestoOptional.get();

                // Reiniciar periodo si expiró
                if (presupuesto.estaExpirado()) {
                    presupuesto.reiniciarPeriodo();
                }
                // Acumular gasto
                presupuesto.agregarGasto(request.monto());
                // Evaluar alertas
                if (presupuesto.estaExcedido()) {
                    alerta = String.format("Has excedido el límite del presupuesto en %.2f",
                            presupuesto.getMontoExcedido());
                } else if (presupuesto.estaCercaDelLimite()) {
                    alerta = String.format("Ya has usado el %.2f%% del presupuesto",
                            presupuesto.getPorcentajeUsado());
                } else {
                    alerta = "Estas dentro del presupuesto";
                }
                presupuestoRepository.guardar(presupuesto);
            }
        }

        // Agregar transacción a la cuenta
        cuenta.agregarTransaccion(transaccion);

        // Guardar
        usuarioRepository.guardar(usuario);

        return new RegistrarTransaccionResponse(
                cuenta.getIdCuenta(),
                cuenta.getSaldo(),
                alerta);
    }
}
