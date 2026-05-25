package com.fabrica.gestionfinancierapersonal.application.usecases;

import org.springframework.stereotype.Service;
import com.fabrica.gestionfinancierapersonal.application.dtos.CrearCuentaRequest;
import com.fabrica.gestionfinancierapersonal.application.dtos.CrearCuentaResponse;
import com.fabrica.gestionfinancierapersonal.application.repository.UsuarioRepository;
import com.fabrica.gestionfinancierapersonal.domain.enums.Moneda;
import com.fabrica.gestionfinancierapersonal.domain.enums.TipoCuenta;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.CampoObligatorioException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.MontoMayorException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.cuenta.TieneCuentaEfectivoException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.usuario.UsuarioNoEncontradoException;
import com.fabrica.gestionfinancierapersonal.domain.model.Cuenta;
import com.fabrica.gestionfinancierapersonal.domain.model.Usuario;
import com.fabrica.gestionfinancierapersonal.domain.validators.ConvertidorEnums;

@Service
public class CrearCuenta {

    private final UsuarioRepository usuarioRepository;
    private final ConvertidorEnums convertidorEnums;

    public CrearCuenta(UsuarioRepository usuarioRepository, ConvertidorEnums convertidorEnums) {
        this.usuarioRepository = usuarioRepository;
        this.convertidorEnums = convertidorEnums;
    }

    public CrearCuentaResponse ejecutar(CrearCuentaRequest request) {

        // Validaciones Basicas
        if (request.idUsuario() == null) {
            throw new CampoObligatorioException("El ID del usuario es obligatorio");
        }

        if (request.saldoInicial() < 0) {
            throw new MontoMayorException("El saldo inicial debe ser cero o positivo");
        }

        // Convertir STRING a ENUM 
        TipoCuenta tipo = convertidorEnums.convertirATipoCuenta(request.tipo());
        Moneda moneda = convertidorEnums.convertirAMoneda(request.moneda());


        if (tipo == TipoCuenta.BANCARIA &&
                (request.nombre() == null || request.nombre().isBlank())) {
            throw new CampoObligatorioException("El nombre es obligatorio para cuentas bancarias");
        }

        // Buscar usuario
        Usuario usuario = usuarioRepository.buscarPorId(request.idUsuario())
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + request.idUsuario()));

        if (tipo == TipoCuenta.EFECTIVO) {
            boolean yaTieneEfectivo = usuario.getCuentas().stream()
                    .anyMatch(c -> c.getTipo() == TipoCuenta.EFECTIVO);

            if (yaTieneEfectivo) {
                throw new TieneCuentaEfectivoException("El usuario ya tiene una cuenta de efectivo");
            }
        }

        // Crear cuenta
        Cuenta cuenta = new Cuenta(
                request.nombre(), 
                tipo,
                moneda, 
                usuario);

        if (request.saldoInicial() > 0) {
            cuenta.registrarSaldoInicial(request.saldoInicial());
        }

        // Agregar al usuario
        usuario.agregarCuenta(cuenta);

        // Guardar
        usuarioRepository.guardar(usuario);

        return new CrearCuentaResponse(
                cuenta.getIdCuenta(),
                cuenta.getNombre(),
                cuenta.getTipo(),
                cuenta.getSaldo(),
                cuenta.getMoneda());
    }
}