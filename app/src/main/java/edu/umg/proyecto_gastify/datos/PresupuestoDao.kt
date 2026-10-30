package edu.umg.proyecto_gastify.datos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PresupuestoDao {

    // Crea el presupuesto o modifica el del mismo mes.
    @Upsert
    suspend fun guardar(presupuesto: PresupuestoEntidad)

    // Observa el presupuesto del mes solicitado.
    @Query("SELECT * FROM presupuestos WHERE mes = :mes")
    fun observarPresupuesto(mes: String): Flow<PresupuestoEntidad?>
}
