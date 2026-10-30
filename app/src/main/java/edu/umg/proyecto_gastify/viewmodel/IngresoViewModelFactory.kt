package edu.umg.proyecto_gastify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.umg.proyecto_gastify.datos.IngresoRepositorio

// Entrega el repositorio al crear el ViewModel.
class IngresoViewModelFactory(
    private val repositorio: IngresoRepositorio
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IngresoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IngresoViewModel(repositorio) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}