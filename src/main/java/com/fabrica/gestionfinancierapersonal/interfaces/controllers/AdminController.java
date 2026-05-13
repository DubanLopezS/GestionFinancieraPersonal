package com.fabrica.gestionfinancierapersonal.interfaces.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fabrica.gestionfinancierapersonal.application.dtos.UsuarioListadoResponse;
import com.fabrica.gestionfinancierapersonal.application.exceptions.AccesoDenegadoException;
import com.fabrica.gestionfinancierapersonal.application.exceptions.UsuarioNoEncontradoException;
import com.fabrica.gestionfinancierapersonal.application.usecases.ListarUsuariosUseCase;

@RestController
@RequestMapping("api/admin")
public class AdminController {

    private final ListarUsuariosUseCase listarUsuariosUseCase;

    public AdminController(ListarUsuariosUseCase listarUsuariosUseCase) {
        this.listarUsuariosUseCase = listarUsuariosUseCase;
    }

    @GetMapping("/usuarios")
    public ResponseEntity<Page<UsuarioListadoResponse>> listarUsuarios(
            @RequestParam UUID idAdmin,
            @RequestParam(required = false) String filtro,
            @PageableDefault(size = 10) Pageable pageable) {

        try {
            return ResponseEntity.ok(listarUsuariosUseCase.ejecutar(
                    idAdmin,
                    filtro,
                    pageable));
        } catch (UsuarioNoEncontradoException e) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage());

        } catch (AccesoDenegadoException e) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    e.getMessage());
        }
    }
}
