package edu.umg.proyecto_gastify.datos

import edu.umg.proyecto_gastify.modelo.Ingreso
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Conecta el modelo Ingreso con SQLite.
class IngresoRepositorio(
    private val ingresoDao: IngresoDao
) {

    // Guarda el ingreso y devuelve el ID generado.
    suspend fun guardar(ingreso: Ingreso): Long {
        val entidad = IngresoEntidad(
            montoCentavos = ingreso.montoCentavos,
            descripcion = ingreso.descripcion,
            fuente = ingreso.fuente,
            fechaMillis = ingreso.fechaMillis
        )

        return ingresoDao.insertar(entidad)
    }

    // Recibe los ingresos y los convierte al modelo.
    fun observarIngresos(): Flow<List<Ingreso>> {
        return ingresoDao.observarIngresos().map { entidades ->
            entidades.map { entidad ->
                Ingreso(
                    montoCentavos = entidad.montoCentavos,
                    descripcion = entidad.descripcion,
                    fuente = entidad.fuente,
                    fechaMillis = entidad.fechaMillis
                )
            }
        }
    }
}