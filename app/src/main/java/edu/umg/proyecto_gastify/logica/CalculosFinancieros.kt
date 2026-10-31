package edu.umg.proyecto_gastify.logica

import edu.umg.proyecto_gastify.modelo.Gasto
import java.math.BigDecimal
import java.util.Calendar
import edu.umg.proyecto_gastify.modelo.Ingreso
import edu.umg.proyecto_gastify.modelo.MovimientoResumen

// Suma únicamente los gastos del mes actual del teléfono.
fun calcularGastosDelMes(gastos: List<Gasto>): BigDecimal {
    val calendario = Calendar.getInstance()

    calendario.set(Calendar.DAY_OF_MONTH, 1)
    calendario.set(Calendar.HOUR_OF_DAY, 0)
    calendario.set(Calendar.MINUTE, 0)
    calendario.set(Calendar.SECOND, 0)
    calendario.set(Calendar.MILLISECOND, 0)

    val inicioMes = calendario.timeInMillis

    calendario.add(Calendar.MONTH, 1)
    val inicioMesSiguiente = calendario.timeInMillis

    var total = BigDecimal.ZERO

    for (gasto in gastos) {
        if (gasto.fechaMillis >= inicioMes &&
            gasto.fechaMillis < inicioMesSiguiente
        ) {
            total = total.add(
                BigDecimal.valueOf(gasto.montoCentavos, 2)
            )
        }
    }

    return total.setScale(2)
}
// Suma los ingresos del mes actual del teléfono.
fun calcularIngresosDelMes(ingresos: List<Ingreso>): BigDecimal {
    val calendario = Calendar.getInstance()

    calendario.set(Calendar.DAY_OF_MONTH, 1)
    calendario.set(Calendar.HOUR_OF_DAY, 0)
    calendario.set(Calendar.MINUTE, 0)
    calendario.set(Calendar.SECOND, 0)
    calendario.set(Calendar.MILLISECOND, 0)

    val inicioMes = calendario.timeInMillis

    calendario.add(Calendar.MONTH, 1)
    val inicioMesSiguiente = calendario.timeInMillis

    var total = BigDecimal.ZERO

    for (ingreso in ingresos) {
        if (ingreso.fechaMillis >= inicioMes &&
            ingreso.fechaMillis < inicioMesSiguiente
        ) {
            total = total.add(
                BigDecimal.valueOf(ingreso.montoCentavos, 2)
            )
        }
    }

    return total.setScale(2)
}

// Combina ambos tipos de movimiento y devuelve los tres más recientes.
fun obtenerUltimosMovimientos(
    gastos: List<Gasto>,
    ingresos: List<Ingreso>,
    limite: Int = 3
): List<MovimientoResumen> {
    val movimientos = mutableListOf<MovimientoResumen>()

    for (gasto in gastos) {
        movimientos.add(
            MovimientoResumen(
                descripcion = gasto.descripcion,
                detalle = gasto.categoria,
                montoCentavos = gasto.montoCentavos,
                fechaMillis = gasto.fechaMillis,
                esIngreso = false
            )
        )
    }

    for (ingreso in ingresos) {
        movimientos.add(
            MovimientoResumen(
                descripcion = ingreso.descripcion,
                detalle = ingreso.fuente,
                montoCentavos = ingreso.montoCentavos,
                fechaMillis = ingreso.fechaMillis,
                esIngreso = true
            )
        )
    }

    return movimientos
        .sortedByDescending { it.fechaMillis }
        .take(limite)
}