
    package edu.umg.proyecto_gastify.datos

    import androidx.room.Entity
    import androidx.room.PrimaryKey

    // Define la tabla de ingresos.
    @Entity(tableName = "ingresos")
    data class IngresoEntidad(
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        val montoCentavos: Long,
        val descripcion: String,
        val fuente: String,
        val fechaMillis: Long
    )
