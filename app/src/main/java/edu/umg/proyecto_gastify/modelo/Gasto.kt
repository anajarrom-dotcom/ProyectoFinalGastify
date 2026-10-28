package edu.umg.proyecto_gastify.modelo

// Representa los datos de un gasto.
data class Gasto(
    val montoCentavos: Long,
    val descripcion: String,
    val categoria: String,
    val fechaMillis: Long
)