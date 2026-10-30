package edu.umg.proyecto_gastify.modelo

// Representa un ingreso de dinero.
data class Ingreso(
    val montoCentavos: Long,
    val descripcion: String,
    val fuente: String,
    val fechaMillis: Long
)
