package edu.umg.proyecto_gastify.viewmodel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.umg.proyecto_gastify.datos.PresupuestoRepositorio
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PresupuestoViewModel(
    private val repositorio: PresupuestoRepositorio
) : ViewModel() {

    // Mes al que corresponde este presupuesto.
    val mes = SimpleDateFormat(
        "yyyy-MM",
        Locale.ROOT
    ).format(Date())

    var montoCentavos by mutableStateOf<Long?>(null)
        private set

    var cargando by mutableStateOf(true)
        private set

    var guardando by mutableStateOf(false)
        private set

    var guardado by mutableStateOf(false)
        private set

    var errorCarga by mutableStateOf<String?>(null)
        private set

    var errorGuardado by mutableStateOf<String?>(null)
        private set

    // Recibe el presupuesto y sus modificaciones.
    init {
        viewModelScope.launch {
            repositorio.observarMonto(mes)
                .catch { error ->
                    if (error is CancellationException) throw error

                    errorCarga = "No se pudo cargar el presupuesto."
                    cargando = false
                }
                .collect { monto ->
                    montoCentavos = monto
                    errorCarga = null
                    cargando = false
                }
        }
    }

    // Guarda un importe positivo expresado en centavos.
    fun guardar(nuevoMontoCentavos: Long) {
        if (guardando || guardado) return

        if (nuevoMontoCentavos <= 0) {
            errorGuardado = "El presupuesto debe ser mayor que cero."
            return
        }

        guardando = true
        errorGuardado = null

        viewModelScope.launch {
            try {
                repositorio.guardar(mes, nuevoMontoCentavos)
                guardado = true
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                errorGuardado = "No se pudo guardar el presupuesto."
            } finally {
                guardando = false
            }
        }
    }

    // Prepara una nueva edición del presupuesto.
    fun prepararEdicion() {
        if (guardando) return

        guardado = false
        errorGuardado = null
    }
}
