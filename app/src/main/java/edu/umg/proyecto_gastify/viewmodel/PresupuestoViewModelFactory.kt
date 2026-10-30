package edu.umg.proyecto_gastify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.umg.proyecto_gastify.datos.PresupuestoRepositorio

// Crea el ViewModel con su repositorio.
class PresupuestoViewModelFactory(
    private val repositorio: PresupuestoRepositorio
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PresupuestoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PresupuestoViewModel(repositorio) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}
