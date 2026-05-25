package com.fabrica.gestionfinancierapersonal.application.usecases;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fabrica.gestionfinancierapersonal.application.dtos.ComparacionGastosMesesResponse;
import com.fabrica.gestionfinancierapersonal.application.exceptions.UsuarioNoEncontradoException;
import com.fabrica.gestionfinancierapersonal.application.repository.TransaccionRepository;
import com.fabrica.gestionfinancierapersonal.application.repository.UsuarioRepository;

@Service
public class CompararGastosMensuales {

    private final UsuarioRepository usuarioRepository;
    private final TransaccionRepository transaccionRepository;

    public CompararGastosMensuales(UsuarioRepository usuarioRepository, TransaccionRepository transaccionRepository) {

        this.usuarioRepository = usuarioRepository;
        this.transaccionRepository = transaccionRepository;
    }

    public ComparacionGastosMesesResponse ejecutar(UUID idUsuario) {

        usuarioRepository.buscarPorId(idUsuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        LocalDate hoy = LocalDate.now();
        YearMonth mesActual = YearMonth.from(hoy);
        YearMonth mesAnterior = mesActual.minusMonths(1);

        double gastoActual = transaccionRepository.sumarGastosPorMes(
                idUsuario,
                mesActual.getMonthValue(),
                mesActual.getYear());

        double gastoAnterior = transaccionRepository.sumarGastosPorMes(
                idUsuario,
                mesAnterior.getMonthValue(),
                mesAnterior.getYear());

        double diferencia = gastoActual - gastoAnterior;


        String mensaje;
        
        if (gastoActual == 0 && gastoAnterior == 0) {
            mensaje = "No existen gastos registrados para comparar";

        } else if (gastoAnterior == 0) {
            mensaje = "No existen gastos registrados el mes anterior";

        } else if (gastoActual > gastoAnterior) {
            mensaje = "Tus gastos aumentaron frente al mes pasado";

        } else if (gastoActual < gastoAnterior) {

            mensaje = "Tus gastos hasta ahora son menores frente al mes pasado";

        } else {
            mensaje = "Tus gastos se mantuvieron igual";
        }

        return new ComparacionGastosMesesResponse(
                gastoActual,
                gastoAnterior,
                diferencia,
                mensaje);
    }
}
