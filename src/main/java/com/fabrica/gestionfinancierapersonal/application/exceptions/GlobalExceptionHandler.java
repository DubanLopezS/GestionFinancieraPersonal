package com.fabrica.gestionfinancierapersonal.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fabrica.gestionfinancierapersonal.domain.exceptions.AccesoDenegadoException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.CampoObligatorioException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.MontoMayorException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.categoria.CategoriaInvalidaParaTransaccionException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.categoria.CategoriaNoEncontradaException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.categoria.CategoriaNoExistenteException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.categoria.NombreCategoriaEnUsoException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.cuenta.CuentaNoExistenteException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.cuenta.CuentasDiferentesException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.cuenta.TieneCuentaEfectivoException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.presupuesto.LimitePresupuestoInvalidoException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.presupuesto.PresupuestoDuplicadoException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.presupuesto.PresupuestoNoEncontradoException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.transaccion.SaldoInsuficienteException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.transaccion.TransaccionNoExistenteException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.usuario.CodigoInvalidoException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.usuario.ContrasenasNoCoincidenException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.usuario.CorreoNoRegistradoException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.usuario.CorreoYaRegistradoException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.usuario.CredencialesInvalidasException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.usuario.TelefonoYaRegistradoException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.usuario.UsernameYaExisteException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.usuario.UsuarioNoEncontradoException;

@ControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(UsuarioNoEncontradoException.class)
        public ResponseEntity<String> manejarUsuarioNoEncontrado(
                        UsuarioNoEncontradoException ex) {
                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(ex.getMessage());
        }

        @ExceptionHandler(CategoriaNoEncontradaException.class)
        public ResponseEntity<String> manejarCategoriaNoEncontrada(
                        CategoriaNoEncontradaException ex) {
                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(ex.getMessage());
        }

        @ExceptionHandler(AccesoDenegadoException.class)
        public ResponseEntity<String> manejarAccesoDenegado(
                        AccesoDenegadoException ex) {
                return ResponseEntity
                                .status(HttpStatus.FORBIDDEN)
                                .body(ex.getMessage());
        }

        @ExceptionHandler(PresupuestoNoEncontradoException.class)
        public ResponseEntity<String> manejarPresupuestoNoEncontrado(
                        PresupuestoNoEncontradoException ex) {
                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(ex.getMessage());
        }

        @ExceptionHandler(PresupuestoDuplicadoException.class)
        public ResponseEntity<String> manejarPresupuestoDuplicado(
                        PresupuestoDuplicadoException ex) {
                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(ex.getMessage());
        }

        @ExceptionHandler(LimitePresupuestoInvalidoException.class)
        public ResponseEntity<String> manejarLimitePresupuestoInvalido(
                        LimitePresupuestoInvalidoException ex) {
                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(ex.getMessage());
        }

        @ExceptionHandler(CorreoYaRegistradoException.class)
        public ResponseEntity<String> manejarCorreoYaRegistrado(
                        CorreoYaRegistradoException ex) {
                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(ex.getMessage());
        }

        @ExceptionHandler(TelefonoYaRegistradoException.class)
        public ResponseEntity<String> manejarTelefonoYaRegistrado(
                        TelefonoYaRegistradoException ex) {
                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(ex.getMessage());
        }

        @ExceptionHandler(UsernameYaExisteException.class)
        public ResponseEntity<String> manejarUsernameYaExiste(
                        UsernameYaExisteException ex) {
                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(ex.getMessage());
        }

        @ExceptionHandler(CredencialesInvalidasException.class)
        public ResponseEntity<String> manejarCredencialesInvalidas(
                        CredencialesInvalidasException ex) {
                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(ex.getMessage());
        }

        @ExceptionHandler(CorreoNoRegistradoException.class)
        public ResponseEntity<String> manejarCorreoNoRegistrado(
                        CorreoNoRegistradoException ex) {
                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(ex.getMessage());
        }

        @ExceptionHandler(CodigoInvalidoException.class)
        public ResponseEntity<String> manejarCodigoInvalido(
                        CodigoInvalidoException ex) {
                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(ex.getMessage());
        }

        @ExceptionHandler(ContrasenasNoCoincidenException.class)
        public ResponseEntity<String> manejarContrasenasNoCoinciden(
                        ContrasenasNoCoincidenException ex) {
                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(ex.getMessage());
        }

        @ExceptionHandler(CampoObligatorioException.class)
        public ResponseEntity<String> manejarCampoObligatorio(
                        CampoObligatorioException ex) {
                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(ex.getMessage());
        }

        @ExceptionHandler(MontoMayorException.class)
        public ResponseEntity<String> manejarMontoMayor(
                        MontoMayorException ex) {
                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(ex.getMessage());
        }

        @ExceptionHandler(NombreCategoriaEnUsoException.class)
        public ResponseEntity<String> manejarNombreCategoriaEnUso(
                        NombreCategoriaEnUsoException ex) {
                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(ex.getMessage());
        }

        @ExceptionHandler(TieneCuentaEfectivoException.class)
        public ResponseEntity<String> manejarTieneCuentaEfectivo(
                        TieneCuentaEfectivoException ex) {
                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(ex.getMessage());
        }

        @ExceptionHandler(CategoriaInvalidaParaTransaccionException.class)
        public ResponseEntity<String> manejarCategoriaInvalidaParaTransaccion(
                        CategoriaInvalidaParaTransaccionException ex) {
                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(ex.getMessage());
        }

        @ExceptionHandler(CuentaNoExistenteException.class)
        public ResponseEntity<String> manejarCuentaNoExistente(
                        CuentaNoExistenteException ex) {
                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(ex.getMessage());
        }

        @ExceptionHandler(CategoriaNoExistenteException.class)
        public ResponseEntity<String> manejarCategoriaNoExistente(
                        CategoriaNoExistenteException ex) {
                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(ex.getMessage());
        }

        @ExceptionHandler(CuentasDiferentesException.class)
        public ResponseEntity<String> manejarCuentasDiferentes(
                        CuentasDiferentesException ex) {
                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(ex.getMessage());
        }

        @ExceptionHandler(SaldoInsuficienteException.class)
        public ResponseEntity<String> manejarSaldoInsuficiente(
                        SaldoInsuficienteException ex) {
                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(ex.getMessage());
        }

        @ExceptionHandler(TransaccionNoExistenteException.class)
        public ResponseEntity<String> manejarTransaccionNoExistente(
                        TransaccionNoExistenteException ex) {
                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(ex.getMessage());
        }
}
