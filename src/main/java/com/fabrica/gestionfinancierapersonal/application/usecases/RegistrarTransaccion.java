package com.fabrica.gestionfinancierapersonal.application.usecases;

import org.springframework.stereotype.Service;

import com.fabrica.gestionfinancierapersonal.application.dtos.RegistrarTransaccionRequest;
import com.fabrica.gestionfinancierapersonal.application.dtos.RegistrarTransaccionResponse;
import com.fabrica.gestionfinancierapersonal.application.repository.CategoriaRepository;
import com.fabrica.gestionfinancierapersonal.application.repository.UsuarioRepository;
import com.fabrica.gestionfinancierapersonal.domain.enums.Periodicidad;
import com.fabrica.gestionfinancierapersonal.domain.enums.TipoTransaccion;
import com.fabrica.gestionfinancierapersonal.domain.model.Categoria;
import com.fabrica.gestionfinancierapersonal.domain.model.Cuenta;
import com.fabrica.gestionfinancierapersonal.domain.model.Transaccion;
import com.fabrica.gestionfinancierapersonal.domain.model.Usuario;
import com.fabrica.gestionfinancierapersonal.domain.validators.ConvertidorEnums;

@Service
public class RegistrarTransaccion {

    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final ConvertidorEnums convertidorEnums;

    public RegistrarTransaccion(UsuarioRepository usuarioRepository, CategoriaRepository categoriaRepository, ConvertidorEnums convertidorEnums) {
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.convertidorEnums = convertidorEnums;
    }

    public RegistrarTransaccionResponse ejecutar(RegistrarTransaccionRequest request) {

        // Validaciones
        if (request.idUsuario() == null) {
            throw new IllegalArgumentException("El id del usuario es obligatorio");
        }

        if (request.idCuenta() == null) {
            throw new IllegalArgumentException("El id de la cuenta es obligatorio");
        }

        if (request.monto() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }

        if (request.tipoTransaccion() == null || request.tipoTransaccion().isBlank()) {
            throw new IllegalArgumentException("El tipo es obligatorio");
        }

        if (request.periodicidad() == null || request.periodicidad().isBlank()) {
            throw new IllegalArgumentException("La periodicidad es obligatoria");
        }

        if (request.idCategoria() == null) {
            throw new IllegalArgumentException("La categoría es obligatoria");
        }

        // Convertir STRING → ENUM
        TipoTransaccion tipo = convertidorEnums.convertirATipoTransaccion(request.tipoTransaccion());

        Periodicidad periodicidad = convertidorEnums.convertirAPeriodicidad(request.periodicidad());
        

        // Buscar usuario
        Usuario usuario = usuarioRepository.buscarPorId(request.idUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Buscar cuenta
        Cuenta cuenta = usuario.buscarCuentaPorId(request.idCuenta());

        Categoria categoria = categoriaRepository
                .buscarPorIdYUsuario(request.idCategoria(), request.idUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        TipoTransaccion tipoTransaccion = TipoTransaccion.valueOf(request.tipoTransaccion().toUpperCase());

        if (!categoria.getTipo().equals(tipoTransaccion)) {
            throw new IllegalArgumentException("La categoría no coincide con el tipo de transacción");
        }

        // Crear transacción
        Transaccion transaccion = new Transaccion(
                request.monto(),
                tipo,
                periodicidad,
                cuenta,
                categoria);

        // Agregar a la cuenta
        cuenta.agregarTransaccion(transaccion);

        // Guardar
        usuarioRepository.guardar(usuario);

        return new RegistrarTransaccionResponse(
                cuenta.getIdCuenta(),
                cuenta.getSaldo());
    }
}
