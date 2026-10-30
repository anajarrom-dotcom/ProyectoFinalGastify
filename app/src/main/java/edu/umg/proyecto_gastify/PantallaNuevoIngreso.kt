package edu.umg.proyecto_gastify


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.umg.proyecto_gastify.logica.validarIngreso
import edu.umg.proyecto_gastify.modelo.Ingreso
import edu.umg.proyecto_gastify.viewmodel.IngresoViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PantallaNuevoIngreso(
    onVolver: () -> Unit,
    ingresoViewModel: IngresoViewModel,
    modifier: Modifier = Modifier
) {
    // Datos del formulario.
    var cantidad by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var fuente by rememberSaveable { mutableStateOf("") }
    var mensaje by rememberSaveable { mutableStateOf("") }
    var menuAbierto by remember { mutableStateOf(false) }

    // Fecha automática del registro.
    val fechaMillis = rememberSaveable {
        System.currentTimeMillis()
    }

    val fecha = remember(fechaMillis) {
        SimpleDateFormat(
            "dd/MM/yyyy",
            Locale("es", "GT")
        ).format(Date(fechaMillis))
    }

    val fuentes = listOf(
        "Salario",
        "Trabajo adicional",
        "Negocio",
        "Regalo",
        "Otros"
    )

    // Regresa después de guardar correctamente.
    LaunchedEffect(ingresoViewModel.ingresoGuardado) {
        if (ingresoViewModel.ingresoGuardado) {
            onVolver()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        TextButton(
            onClick = onVolver,
            enabled = !ingresoViewModel.guardando
        ) {
            Text("← Volver", color = Color(0xFF3478C0))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Registrar ingreso",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF142D4E)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Completa los datos de tu ingreso.",
            fontSize = 16.sp,
            color = Color(0xFF607080)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Monto y descripción en la misma fila.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = cantidad,
                onValueChange = { nuevoValor ->
                    if (nuevoValor.matches(Regex("^\\d*(\\.\\d{0,2})?$"))) {
                        cantidad = nuevoValor
                        mensaje = ""
                    }
                },
                modifier = Modifier.weight(1f),
                label = { Text("Monto (Q)") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                enabled = !ingresoViewModel.guardando,
                singleLine = true
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = {
                    if (it.length <= 200) {
                        descripcion = it
                        mensaje = ""
                    }
                },
                modifier = Modifier.weight(1f),
                label = { Text("Descripción") },
                enabled = !ingresoViewModel.guardando,
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Fuente del ingreso",
            fontWeight = FontWeight.Medium,
            color = Color(0xFF142D4E)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Selección del origen del dinero.
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { menuAbierto = true },
                modifier = Modifier.fillMaxWidth(),
                enabled = !ingresoViewModel.guardando
            ) {
                Text(
                    if (fuente.isBlank()) {
                        "Seleccionar fuente ▾"
                    } else {
                        "$fuente ▾"
                    }
                )
            }

            DropdownMenu(
                expanded = menuAbierto,
                onDismissRequest = { menuAbierto = false }
            ) {
                fuentes.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            fuente = opcion
                            menuAbierto = false
                            mensaje = ""
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = fecha,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Fecha del ingreso") },
            readOnly = true,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Valida y entrega el ingreso al ViewModel.
        Button(
            onClick = {
                val error = validarIngreso(
                    cantidad,
                    fuente,
                    descripcion
                )

                if (error != null) {
                    mensaje = error
                } else {
                    mensaje = ""

                    val ingreso = Ingreso(
                        montoCentavos = cantidad.toBigDecimal()
                            .movePointRight(2)
                            .longValueExact(),
                        descripcion = descripcion.trim(),
                        fuente = fuente,
                        fechaMillis = fechaMillis
                    )

                    ingresoViewModel.guardar(ingreso)
                }
            },
            enabled = !ingresoViewModel.guardando &&
                    !ingresoViewModel.ingresoGuardado,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2E9D72)
            )
        ) {
            Text(
                text = if (ingresoViewModel.guardando) {
                    "Guardando..."
                } else {
                    "Guardar ingreso"
                },
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        if (mensaje.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(mensaje, color = Color(0xFFB3261E))
        }

        ingresoViewModel.errorGuardado?.let { error ->
            Spacer(modifier = Modifier.height(12.dp))
            Text(error, color = Color(0xFFB3261E))
        }
    }
}