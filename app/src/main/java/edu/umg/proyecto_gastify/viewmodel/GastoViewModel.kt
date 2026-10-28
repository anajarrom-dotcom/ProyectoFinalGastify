package edu.umg.proyecto_gastify.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.umg.proyecto_gastify.datos.GastoRepositorio
import edu.umg.proyecto_gastify.modelo.Gasto
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect

// Coordina el guardado y mantiene su estado para la pantalla.
class GastoViewModel(
    private val repositorio: GastoRepositorio
) : ViewModel() {

    var guardando by mutableStateOf(false)
        private set

    var gastoGuardado by mutableStateOf(false)
        private set

    var errorGuardado by mutableStateOf<String?>(null)
        private set

    // Lista de gastos obtenida de SQLite.
    var gastos by mutableStateOf<List<Gasto>>(emptyList())
        private set

    // Indica si estamos esperando la primera consulta.
    var cargandoGastos by mutableStateOf(true)
        private set

    // Mensaje si ocurre un error al consultar.
    var errorCarga by mutableStateOf<String?>(null)
        private set

    // Se ejecuta cuando se crea el ViewModel.
    init {
        viewModelScope.launch {
            repositorio.observarGastos()
                .catch { error ->
                    if (error is CancellationException) {
                        throw error
                    }

                    errorCarga = "No se pudieron cargar los gastos."
                    cargandoGastos = false
                }
                .collect { lista ->
                    gastos = lista
                    errorCarga = null
                    cargandoGastos = false
                }
        }
    }

    // Guarda sin bloquear la interfaz.
    fun guardar(gasto: Gasto) {
        if (guardando || gastoGuardado) return

        guardando = true
        errorGuardado = null

        viewModelScope.launch {
            try {
                repositorio.guardar(gasto)
                gastoGuardado = true
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                errorGuardado = "No se pudo guardar el gasto. Intenta nuevamente."
            } finally {
                guardando = false
            }
        }
    }

    // Prepara el estado cuando comenzamos un nuevo registro.
    fun prepararNuevoGasto() {
        if (guardando) return

        gastoGuardado = false
        errorGuardado = null
    }
}
