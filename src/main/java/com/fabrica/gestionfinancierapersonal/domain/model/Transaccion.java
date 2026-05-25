package com.fabrica.gestionfinancierapersonal.domain.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;
import com.fabrica.gestionfinancierapersonal.domain.enums.Periodicidad;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.CampoObligatorioException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.MontoMayorException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.categoria.CategoriaInvalidaParaTransaccionException;
import com.fabrica.gestionfinancierapersonal.domain.enums.TipoTransaccion;

import jakarta.persistence.*;

@Entity
@Table(name = "transacciones")
@Getter
@Setter
public class Transaccion {

    @Id
    private UUID idTransaccion;

    private double monto;

    @Enumerated(EnumType.STRING)
    private TipoTransaccion tipo;

    @Enumerated(EnumType.STRING)
    private Periodicidad periodicidad;

    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "cuenta_id")
    private Cuenta cuenta;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    private UUID transferenciaId;

    protected Transaccion() {
    }

    public Transaccion(double monto, TipoTransaccion tipo, Periodicidad periodicidad, Cuenta cuenta,
            Categoria categoria, UUID transferenciaId) {

        if (monto <= 0) {
            throw new MontoMayorException("El monto debe ser mayor a 0");
        }

        if (tipo == null) {
            throw new CampoObligatorioException("El Tipo es obligatorio");
        }

        if (periodicidad == null) {
            throw new CampoObligatorioException("La Periodicidad es obligatoria");
        }

        if (categoria == null) {
            throw new CampoObligatorioException("La categoría es obligatoria");
        }

        if (!categoria.getTipo().equals(tipo)) {
            throw new CategoriaInvalidaParaTransaccionException("La categoría no coincide con el tipo de transacción");
        }

        this.idTransaccion = UUID.randomUUID();
        this.monto = monto;
        this.tipo = tipo;
        this.periodicidad = periodicidad;
        this.cuenta = cuenta;
        this.categoria = categoria;
        this.fecha = LocalDateTime.now();
        this.transferenciaId = transferenciaId;
    }

    public Transaccion(double monto,
            TipoTransaccion tipo,
            Periodicidad periodicidad,
            Cuenta cuenta,
            Categoria categoria) {

        this(monto, tipo, periodicidad, cuenta, categoria, null);
    }
}
