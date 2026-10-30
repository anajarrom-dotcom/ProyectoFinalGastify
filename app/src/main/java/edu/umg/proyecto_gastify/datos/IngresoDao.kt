package edu.umg.proyecto_gastify.datos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface IngresoDao {

    // Guarda un ingreso y devuelve su identificador.
    @Insert
    suspend fun insertar(ingreso: IngresoEntidad): Long

    // Consulta los ingresos del más reciente al más antiguo.
    @Query("SELECT * FROM ingresos ORDER BY fechaMillis DESC, id DESC")
    fun observarIngresos(): Flow<List<IngresoEntidad>>
}
