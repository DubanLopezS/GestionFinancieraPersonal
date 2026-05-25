package com.fabrica.gestionfinancierapersonal.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.fabrica.gestionfinancierapersonal.domain.enums.TipoCuenta;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.CampoObligatorioException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.MontoMayorException;
import com.fabrica.gestionfinancierapersonal.domain.strategy.OperacionSaldo;
import com.fabrica.gestionfinancierapersonal.domain.strategy.SaldoFactory;
import jakarta.persistence.*;
import com.fabrica.gestionfinancierapersonal.domain.enums.Moneda;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cuentas")
@Getter
@Setter
public class Cuenta {

    @Id
    private UUID idCuenta;

    private String nombre;
    private double saldo;

    @Enumerated(EnumType.STRING)
    private TipoCuenta tipo;

    @Enumerated(EnumType.STRING)
    private Moneda moneda;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL)
    private List<Transaccion> transacciones;

    protected Cuenta() {
    }

    public Cuenta(String nombre, TipoCuenta tipo, Moneda moneda, Usuario usuario) {

        if (tipo == null) {
            throw new CampoObligatorioException("Tipo de cuenta obligatorio");
        }

        if (moneda == null) {
            throw new CampoObligatorioException("La moneda es obligatoria");
        }
        this.tipo = tipo;

        if (tipo == TipoCuenta.BANCARIA) {
            if (nombre == null || nombre.isBlank()) {
                throw new CampoObligatorioException("El nombre es obligatorio para cuentas bancarias");
            }
            this.nombre = nombre;
        } else {
            this.nombre = "Efectivo";
        }
        this.idCuenta = UUID.randomUUID();
        this.saldo = 0.0;
        this.moneda = moneda;
        this.usuario = usuario;
        this.transacciones = new ArrayList<>();
    }

    public void registrarSaldoInicial(double monto) {

        if (monto < 0) {
            throw new MontoMayorException("Saldo inicial no puede ser negativo");
        }

        if (this.saldo != 0) {
            throw new IllegalStateException("El saldo inicial ya fue definido");
        }

        this.saldo = monto;
    }

    public void agregarTransaccion(Transaccion transaccion) {

        validarTransaccion(transaccion);

        OperacionSaldo operacion = SaldoFactory.crear(transaccion.getTipo());

        operacion.aplicar(this, transaccion.getMonto());

        this.transacciones.add(transaccion);
    }

    public void sumarSaldo(double monto) {
        this.saldo += monto;
    }

    public void restarSaldo(double monto) {
        this.saldo -= monto;
    }

    private void validarTransaccion(Transaccion transaccion) {

        if (transaccion == null) {
            throw new CampoObligatorioException("Transacción inválida");
        }

        if (transaccion.getMonto() <= 0) {
            throw new CampoObligatorioException("El monto debe ser mayor a 0");
        }
    }
}