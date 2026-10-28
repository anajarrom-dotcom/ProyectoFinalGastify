package edu.umg.proyecto_gastify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.umg.proyecto_gastify.datos.GastoRepositorio

// Crea el ViewModel y le entrega el repositorio.
class GastoViewModelFactory(
    private val repositorio: GastoRepositorio
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GastoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GastoViewModel(repositorio) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}
