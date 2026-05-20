package com.fabrica.gestionfinancierapersonal.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fabrica.gestionfinancierapersonal.domain.enums.PeriodoPresupuesto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "presupuestos")
@Getter
@Setter
@NoArgsConstructor
public class Presupuesto {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @Column(nullable = false)
    private double limite;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PeriodoPresupuesto periodo;

    @Column(nullable = false)
    private Boolean activo;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private LocalDateTime fechaExpiracion;

    @Column(nullable = false)
    private double montoGastado;

    public Presupuesto(Usuario usuario, Categoria categoria, double limite, PeriodoPresupuesto periodo,
            LocalDateTime fechaCreacion) {

        this.id = UUID.randomUUID();
        this.usuario = usuario;
        this.categoria = categoria;
        this.limite = limite;
        this.periodo = periodo;
        this.activo = true;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaCreacion = fechaCreacion;
        this.montoGastado = 0;
    }

    public void agregarGasto(double monto) {
        this.montoGastado += monto;
    }

    public double getRestante() {
        return limite - montoGastado;
    }

    public double getPorcentajeUsado() {
        return (montoGastado / limite) * 100;
    }
}
