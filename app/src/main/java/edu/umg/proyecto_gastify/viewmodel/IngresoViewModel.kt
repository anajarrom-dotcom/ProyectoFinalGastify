package edu.umg.proyecto_gastify.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.umg.proyecto_gastify.datos.IngresoRepositorio
import edu.umg.proyecto_gastify.modelo.Ingreso
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

// Administra los ingresos y el estado del guardado.
class IngresoViewModel(
    private val repositorio: IngresoRepositorio
) : ViewModel() {

    var ingresos by mutableStateOf<List<Ingreso>>(emptyList())
        private set

    var cargandoIngresos by mutableStateOf(true)
        private set

    var errorCarga by mutableStateOf<String?>(null)
        private set

    var guardando by mutableStateOf(false)
        private set

    var ingresoGuardado by mutableStateOf(false)
        private set

    var errorGuardado by mutableStateOf<String?>(null)
        private set

    // Mantiene actualizada la lista de ingresos.
    init {
        viewModelScope.launch {
            repositorio.observarIngresos()
                .catch { error ->
                    if (error is CancellationException) throw error

                    errorCarga = "No se pudieron cargar los ingresos."
                    cargandoIngresos = false
                }
                .collect { lista ->
                    ingresos = lista
                    errorCarga = null
                    cargandoIngresos = false
                }
        }
    }

    // Guarda y evita solicitudes repetidas.
    fun guardar(ingreso: Ingreso) {
        if (guardando || ingresoGuardado) return

        guardando = true
        errorGuardado = null

        viewModelScope.launch {
            try {
                repositorio.guardar(ingreso)
                ingresoGuardado = true
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                errorGuardado = "No se pudo guardar. Intenta nuevamente."
            } finally {
                guardando = false
            }
        }
    }

    // Limpia el estado antes de abrir otro formulario.
    fun prepararNuevoIngreso() {
        if (guardando) return

        ingresoGuardado = false
        errorGuardado = null
    }
}