package com.fabrica.gestionfinancierapersonal.domain.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class Usuario {

    private String id;
    private String nombre;
    private String correo;
    private String contrasena;
    private String telefono;
    private List<Cuenta> cuentas;

    public Usuario(String id, String nombre, String correo, String contrasena, String telefono) {

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Id obligatorio");
        }
        
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Nombre obligatorio");
        }

        if (correo == null || correo.isBlank()) {
            throw new IllegalArgumentException("Correo obligatorio");
        }

        if (contrasena == null || contrasena.isBlank()) {
            throw new IllegalArgumentException("Contraseña obligatoria");
        }

        if (telefono == null || telefono.isBlank()) {
            throw new IllegalArgumentException("Teléfono obligatorio");
        }

        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.cuentas = new ArrayList<>();
    }

    public void agregarCuenta(Cuenta cuenta) {
        if (cuenta == null) {
            throw new IllegalArgumentException("Cuenta inválida");
        }
        this.cuentas.add(cuenta);
    }
}
