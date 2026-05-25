package com.fabrica.gestionfinancierapersonal.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fabrica.gestionfinancierapersonal.domain.enums.Rol;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.CampoObligatorioException;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
public class Usuario {

    @Id
    private UUID idUsuario;

    private String username;
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasena;
    private String telefono;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Cuenta> cuentas;

    protected Usuario() {
    }

    public Usuario(String username, String nombre, String apellido,
            String correo, String contrasena, String telefono) {

        if (username == null || username.isBlank()) {
            throw new CampoObligatorioException("El Username es obligatorio");
        }

        if (nombre == null || nombre.isBlank()) {
            throw new CampoObligatorioException("El Nombre es obligatorio");
        }

        if (apellido == null || apellido.isBlank()) {
            throw new CampoObligatorioException("El Apellido es obligatorio");
        }

        if (correo == null || correo.isBlank()) {
            throw new CampoObligatorioException("El Correo es obligatorio");
        }

        if (contrasena == null || contrasena.isBlank()) {
            throw new CampoObligatorioException("La Contraseña es obligatoria");
        }

        if (telefono == null || telefono.isBlank()) {
            throw new CampoObligatorioException("El Teléfono es obligatorio");
        }

        this.idUsuario = UUID.randomUUID();
        this.username = username;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.rol = Rol.USUARIO;
        this.cuentas = new ArrayList<>();
    }

    public void agregarCuenta(Cuenta cuenta) {
        if (cuenta == null) {
            throw new CampoObligatorioException("La cuenta no puede ser null");
        }
        boolean existe = this.cuentas.stream()
                .anyMatch(c -> c.getNombre().equalsIgnoreCase(cuenta.getNombre()));
        if (existe) {
            throw new CampoObligatorioException("Ya existe una cuenta con ese nombre");
        }
        this.cuentas.add(cuenta);
    }

    public Cuenta buscarCuentaPorId(UUID IdCuenta) {
        if (IdCuenta == null) {
            throw new CampoObligatorioException("El id de la cuenta es obligatorio");
        }
        return this.cuentas.stream()
                .filter(c -> c.getIdCuenta().equals(IdCuenta))
                .findFirst()
                .orElseThrow(() -> new CampoObligatorioException("Cuenta no encontrada"));
    }
}
