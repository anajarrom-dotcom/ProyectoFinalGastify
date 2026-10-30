package edu.umg.proyecto_gastify

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.umg.proyecto_gastify.viewmodel.PresupuestoViewModel
import java.math.BigDecimal
import java.math.MathContext

@Composable
fun TarjetaPresupuesto(
    totalGastos: BigDecimal,
    gastosDisponibles: Boolean,
    viewModel: PresupuestoViewModel
) {
    // Estado de la ventana de edición.
    var mostrarFormulario by rememberSaveable { mutableStateOf(false) }
    var cantidad by rememberSaveable { mutableStateOf("") }
    var errorFormulario by rememberSaveable { mutableStateOf("") }

    val centavos = viewModel.montoCentavos
    val presupuesto = centavos?.let {
        BigDecimal.valueOf(it, 2)
    }

    // Cierra la ventana después de guardar correctamente.
    LaunchedEffect(viewModel.guardado) {
        if (viewModel.guardado) {
            mostrarFormulario = false
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Presupuesto mensual",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF142D4E)
            )

            Spacer(modifier = Modifier.height(12.dp))

            when {
                viewModel.cargando -> {
                    Text("Cargando presupuesto...")
                }

                viewModel.errorCarga != null -> {
                    Text(
                        text = viewModel.errorCarga ?: "",
                        color = Color(0xFFB3261E)
                    )
                }

                presupuesto == null -> {
                    Text(
                        text = "Sin configurar",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3478C0)
                    )
                    Text("Establece cuánto quieres gastar este mes.")
                }

                else -> {
                    Text(
                        text = "Límite: Q${presupuesto.toPlainString()}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF142D4E)
                    )

                    if (gastosDisponibles && presupuesto.signum() > 0) {
                        val restante = presupuesto.subtract(totalGastos)
                        val limiteAviso = presupuesto.multiply(
                            BigDecimal("0.80")
                        )

                        val colorAviso = when {
                            totalGastos >= presupuesto ->
                                Color(0xFFB3261E)
                            totalGastos >= limiteAviso ->
                                Color(0xFF9A5800)
                            else ->
                                Color(0xFF3478C0)
                        }

                        val aviso = when {
                            totalGastos > presupuesto ->
                                "Superaste tu presupuesto por Q${
                                    restante.abs().toPlainString()
                                }"
                            totalGastos.compareTo(presupuesto) == 0 ->
                                "Has alcanzado tu presupuesto."
                            totalGastos >= limiteAviso ->
                                "Estás cerca de tu límite. Te quedan Q${
                                    restante.toPlainString()
                                }"
                            else ->
                                "Te quedan Q${restante.toPlainString()}"
                        }

                        // La barra se limita al 100 %, aunque exista exceso.
                        val progreso = totalGastos.divide(
                            presupuesto,
                            MathContext.DECIMAL64
                        ).coerceIn(
                            BigDecimal.ZERO,
                            BigDecimal.ONE
                        ).toFloat()

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Gastado: Q${totalGastos.toPlainString()}",
                            color = Color(0xFF263238)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { progreso },
                            modifier = Modifier.fillMaxWidth(),
                            color = colorAviso,
                            trackColor = Color(0xFFE5EAF0)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = aviso,
                            color = colorAviso,
                            fontWeight = FontWeight.Medium
                        )
                    } else {
                        Text("El resumen de gastos aún no está disponible.")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Abre la ventana con el importe actual.
            OutlinedButton(
                onClick = {
                    viewModel.prepararEdicion()
                    cantidad = presupuesto?.toPlainString() ?: ""
                    errorFormulario = ""
                    mostrarFormulario = true
                },
                enabled = !viewModel.cargando &&
                        viewModel.errorCarga == null &&
                        !viewModel.guardando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (presupuesto == null) {
                        "Establecer presupuesto"
                    } else {
                        "Modificar presupuesto"
                    }
                )
            }
        }
    }

    // Ventana para registrar o modificar el presupuesto.
    if (mostrarFormulario) {
        AlertDialog(
            onDismissRequest = {
                if (!viewModel.guardando) {
                    mostrarFormulario = false
                }
            },
            title = {
                Text("Presupuesto · ${viewModel.mes}")
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = cantidad,
                        onValueChange = {
                            if (it.matches(Regex("^\\d*(\\.\\d{0,2})?$"))) {
                                cantidad = it
                                errorFormulario = ""
                            }
                        },
                        label = { Text("Límite mensual (Q)") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        singleLine = true,
                        enabled = !viewModel.guardando
                    )

                    if (errorFormulario.isNotEmpty()) {
                        Text(
                            text = errorFormulario,
                            color = Color(0xFFB3261E)
                        )
                    }

                    viewModel.errorGuardado?.let {
                        Text(it, color = Color(0xFFB3261E))
                    }
                }
            },
            confirmButton = {
                TextButton(
                    enabled = !viewModel.guardando &&
                            !viewModel.guardado,
                    onClick = {
                        val monto = cantidad.toBigDecimalOrNull()

                        if (monto == null || monto.signum() <= 0) {
                            errorFormulario = "Ingresa un monto mayor que cero."
                        } else {
                            try {
                                val montoCentavos = monto
                                    .movePointRight(2)
                                    .longValueExact()

                                errorFormulario = ""
                                viewModel.guardar(montoCentavos)
                            } catch (error: ArithmeticException) {
                                errorFormulario = "El monto no es válido."
                            }
                        }
                    }
                ) {
                    Text(
                        if (viewModel.guardando) "Guardando..." else "Guardar"
                    )
                }
            },
            dismissButton = {
                TextButton(
                    enabled = !viewModel.guardando,
                    onClick = { mostrarFormulario = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
