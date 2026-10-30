package edu.umg.proyecto_gastify.modelo

// Datos comunes para mostrar ingresos y gastos en Inicio.
data class MovimientoResumen(
    val descripcion: String,
    val detalle: String,
    val montoCentavos: Long,
    val fechaMillis: Long,
    val esIngreso: Boolean
)
