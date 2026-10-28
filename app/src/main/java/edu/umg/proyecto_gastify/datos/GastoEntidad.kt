package edu.umg.proyecto_gastify.datos

import androidx.room.Entity
import androidx.room.PrimaryKey

// Define la tabla gastos de la base de datos.
@Entity(tableName = "gastos")
data class GastoEntidad(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val montoCentavos: Long,
    val descripcion: String,
    val categoria: String,
    val fechaMillis: Long
)
