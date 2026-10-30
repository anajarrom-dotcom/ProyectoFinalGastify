package edu.umg.proyecto_gastify.datos

import androidx.room.Entity
import androidx.room.PrimaryKey

// Guarda un presupuesto por cada mes.
@Entity(tableName = "presupuestos")
data class PresupuestoEntidad(
    @PrimaryKey
    val mes: String,
    val montoCentavos: Long
)
