package com.fabrica.gestionfinancierapersonal.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fabrica.gestionfinancierapersonal.application.exceptions.presupuesto.LimitePresupuestoInvalidoException;
import com.fabrica.gestionfinancierapersonal.application.exceptions.presupuesto.PresupuestoDuplicadoException;
import com.fabrica.gestionfinancierapersonal.application.exceptions.presupuesto.PresupuestoNoEncontradoException;

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
}
