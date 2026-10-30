package edu.umg.proyecto_gastify.datos

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Coordina la lectura y el guardado del presupuesto.
class PresupuestoRepositorio(
    private val presupuestoDao: PresupuestoDao
) {
    suspend fun guardar(mes: String, montoCentavos: Long) {
        presupuestoDao.guardar(
            PresupuestoEntidad(
                mes = mes,
                montoCentavos = montoCentavos
            )
        )
    }

    // Devuelve el importe o null si aún no está configurado.
    fun observarMonto(mes: String): Flow<Long?> {
        return presupuestoDao.observarPresupuesto(mes).map {
            it?.montoCentavos
        }
    }
}
