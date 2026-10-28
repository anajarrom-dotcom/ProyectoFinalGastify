package edu.umg.proyecto_gastify.datos

import edu.umg.proyecto_gastify.modelo.Gasto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Coordina el guardado de gastos en la base de datos.
class GastoRepositorio(
    private val gastoDao: GastoDao
) {

    // Convierte el modelo a una entidad y guarda el registro.
    suspend fun guardar(gasto: Gasto): Long {
        val entidad = GastoEntidad(
            montoCentavos = gasto.montoCentavos,
            descripcion = gasto.descripcion,
            categoria = gasto.categoria,
            fechaMillis = gasto.fechaMillis
        )

        return gastoDao.insertar(entidad)
    }
    // Observa los registros de SQLite y los convierte al modelo Gasto.
    fun observarGastos(): Flow<List<Gasto>> {
        return gastoDao.observarGastos().map { entidades ->
            entidades.map { entidad ->
                Gasto(
                    montoCentavos = entidad.montoCentavos,
                    descripcion = entidad.descripcion,
                    categoria = entidad.categoria,
                    fechaMillis = entidad.fechaMillis
                )
            }
        }
    }
}
