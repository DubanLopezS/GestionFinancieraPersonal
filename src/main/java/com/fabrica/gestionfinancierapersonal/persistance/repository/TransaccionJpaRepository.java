package com.fabrica.gestionfinancierapersonal.persistance.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fabrica.gestionfinancierapersonal.domain.model.Categoria;
import com.fabrica.gestionfinancierapersonal.domain.model.Transaccion;

@Repository
public interface TransaccionJpaRepository extends JpaRepository<Transaccion, UUID> {

        @Query("""
                        SELECT t FROM Transaccion t
                        JOIN FETCH t.categoria
                        WHERE t.cuenta.idCuenta = :cuentaId
                        AND t.cuenta.usuario.idUsuario = :usuarioId
                        """)
        List<Transaccion> findByCuentaYUsuario(UUID cuentaId, UUID usuarioId);

        @Query("""
                        SELECT t FROM Transaccion t
                        JOIN FETCH t.categoria
                        WHERE t.cuenta.idCuenta = :cuentaId
                        AND t.categoria.idCategoria = :categoriaId
                        AND t.cuenta.usuario.idUsuario = :usuarioId
                        """)
        List<Transaccion> findByCuentaCategoriaYUsuario(
                        UUID cuentaId,
                        UUID categoriaId,
                        UUID usuarioId);

        @Query("""
                        SELECT COALESCE(SUM(t.monto), 0.0)
                        FROM Transaccion t
                        WHERE t.cuenta.usuario.idUsuario = :usuarioId
                        AND t.tipo = com.fabrica.gestionfinancierapersonal.domain.enums.TipoTransaccion.INGRESO
                        AND t.categoria.esSistema = false
                        AND YEAR(t.fecha) = YEAR(CURRENT_DATE)
                        AND MONTH(t.fecha) = MONTH(CURRENT_DATE)
                        """)
        double sumarIngresosDelMes(UUID usuarioId);

        @Query("""
                        SELECT COALESCE(SUM(t.monto), 0.0)
                        FROM Transaccion t
                        WHERE t.cuenta.usuario.idUsuario = :usuarioId
                        AND t.tipo = com.fabrica.gestionfinancierapersonal.domain.enums.TipoTransaccion.GASTO
                        AND t.categoria.esSistema = false
                        AND YEAR(t.fecha) = YEAR(CURRENT_DATE)
                        AND MONTH(t.fecha) = MONTH(CURRENT_DATE)
                        """)
        double sumarGastosDelMes(UUID usuarioId);

        @Query("""
                        SELECT t.categoria FROM Transaccion t
                        WHERE t.cuenta.usuario.idUsuario = :usuarioId
                        AND t.tipo = com.fabrica.gestionfinancierapersonal.domain.enums.TipoTransaccion.GASTO
                        AND t.categoria.esSistema = false
                        AND YEAR(t.fecha) = YEAR(CURRENT_DATE)
                        AND MONTH(t.fecha) = MONTH(CURRENT_DATE)
                        GROUP BY t.categoria
                        ORDER BY SUM(t.monto) DESC
                        """)
        List<Categoria> obtenerCategoriaMayorGasto(UUID usuarioId);


        @Query("""
                        SELECT COALESCE(SUM(t.monto), 0.0) FROM Transaccion t
                        WHERE t.cuenta.usuario.idUsuario = :usuarioId
                        AND t.categoria.idCategoria = :categoriaId
                        AND t.tipo = com.fabrica.gestionfinancierapersonal.domain.enums.TipoTransaccion.GASTO
                        AND YEAR(t.fecha) = YEAR(CURRENT_DATE)
                        AND MONTH(t.fecha) = MONTH(CURRENT_DATE)
                        """)
        double sumarGastosPorCategoriaDelMes(UUID usuarioId, UUID categoriaId);

        
        @Query("""
                        SELECT t FROM Transaccion t
                        JOIN FETCH t.categoria
                        JOIN FETCH t.cuenta
                        WHERE t.cuenta.usuario.idUsuario = :usuarioId
                        AND t.categoria.idCategoria = :categoriaId
                        AND t.tipo = com.fabrica.gestionfinancierapersonal.domain.enums.TipoTransaccion.GASTO
                        AND t.categoria.esSistema = false
                        AND YEAR(t.fecha) = YEAR(CURRENT_DATE)
                        AND MONTH(t.fecha) = MONTH(CURRENT_DATE)
                        ORDER BY t.fecha DESC
                        """)
        List<Transaccion> obtenerGastosPorCategoria(UUID usuarioId, UUID categoriaId);


        @Query("""
                        SELECT COALESCE(SUM(t.monto), 0.0)
                        FROM Transaccion t
                        WHERE t.cuenta.usuario.idUsuario = :usuarioId
                        AND t.tipo = com.fabrica.gestionfinancierapersonal.domain.enums.TipoTransaccion.GASTO
                        AND t.categoria.esSistema = false
                        AND MONTH(t.fecha) = :mes
                        AND YEAR(t.fecha) = :anio
                        """)
        double sumarGastosPorMes(UUID usuarioId, int mes, int anio);

}