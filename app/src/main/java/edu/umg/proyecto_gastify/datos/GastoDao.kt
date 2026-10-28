package edu.umg.proyecto_gastify.datos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// Operaciones de acceso a la tabla gastos.
@Dao
interface GastoDao {

    // Inserta un gasto y devuelve el ID generado.
    @Insert
    suspend fun insertar(gasto: GastoEntidad): Long

    // Observa los gastos, del más reciente al más antiguo.
    @Query("SELECT * FROM gastos ORDER BY fechaMillis DESC, id DESC")
    fun observarGastos(): Flow<List<GastoEntidad>>
}
