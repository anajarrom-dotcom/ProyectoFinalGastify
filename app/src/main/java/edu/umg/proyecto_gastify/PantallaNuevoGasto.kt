package edu.umg.proyecto_gastify

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.remember
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import edu.umg.proyecto_gastify.logica.validarGasto
import androidx.compose.runtime.LaunchedEffect
import edu.umg.proyecto_gastify.modelo.Gasto
import edu.umg.proyecto_gastify.viewmodel.GastoViewModel

// Estructura inicial de la pantalla para registrar un gasto.
@Composable
fun PantallaNuevoGasto(
    onVolver: () -> Unit,
    gastoViewModel: GastoViewModel,
    modifier: Modifier = Modifier
) {
    // Variable que guarda la cantidad escrita.
    var cantidad by rememberSaveable {
        mutableStateOf("")

    }// Descripción obligatoria del gasto.
    var descripcion by rememberSaveable {
        mutableStateOf("")
    }

// Categoría elegida por el usuario.
    var categoria by rememberSaveable {
        mutableStateOf("")
    }

// Controla si el menú de categorías está abierto.
    var menuCategoriasAbierto by remember {
        mutableStateOf(false)
    }

    /// Fecha que se almacenará con el gasto.
    val fechaMillis = rememberSaveable {
        System.currentTimeMillis()
    }

// Convierte esa fecha al formato visible.
    val fecha = remember(fechaMillis) {
        SimpleDateFormat(
            "dd/MM/yyyy",
            Locale("es", "GT")
        ).format(Date(fechaMillis))
    }

    val categorias = listOf(
        "Supermercado",
        "Alimentación",
        "Transporte",
        "Vivienda",
        "Servicios",
        "Educación",
        "Salud",
        "Entretenimiento",
        "Compras personales",
        "Otros"
    )

    // Mensaje con el resultado de la validación.
    var mensaje by rememberSaveable {
        mutableStateOf("")
    }

// Regresa cuando la base de datos confirma el guardado.
    LaunchedEffect(gastoViewModel.gastoGuardado) {
        if (gastoViewModel.gastoGuardado) {
            onVolver()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .padding(24.dp)
    ) {
        // Ejecuta la acción de regreso que recibirá esta pantalla.
        TextButton(onClick = onVolver) {
            Text(
                text = "← Volver",
                color = Color(0xFF3478C0)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Registrar gasto",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF142D4E)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Completa los datos de tu gasto.",
            fontSize = 16.sp,
            color = Color(0xFF607080)
        )

        Spacer(modifier = Modifier.height(24.dp))

// Campo para ingresar la cantidad del gasto.

        // Monto y descripción, uno al lado del otro.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = cantidad,
                onValueChange = { nuevoValor ->
                    if (nuevoValor.matches(Regex("^\\d*(\\.\\d{0,2})?$"))) {
                        cantidad = nuevoValor
                    }
                },
                modifier = Modifier.weight(1f),
                label = { Text("Monto (Q)") },
                placeholder = { Text("0.00") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                singleLine = true
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { nuevoTexto ->
                    if (nuevoTexto.length <= 200) {
                        descripcion = nuevoTexto
                    }
                },
                modifier = Modifier.weight(1f),
                label = { Text("Descripción") },
                placeholder = { Text("  ") },
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

// Título del selector de categoría.
        Text(
            text = "Categoría",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF142D4E)
        )

        Spacer(modifier = Modifier.height(8.dp))

// Botón y lista desplegable de categorías.
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { menuCategoriasAbierto = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (categoria.isEmpty()) {
                        "Seleccionar categoría ▾"
                    } else {
                        "$categoria ▾"
                    }
                )
            }

            DropdownMenu(
                expanded = menuCategoriasAbierto,
                onDismissRequest = { menuCategoriasAbierto = false }
            ) {
                categorias.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            categoria = opcion
                            menuCategoriasAbierto = false
                        }
                    )
                }
            }
        } // Cierra el Box de categorías.

        Spacer(modifier = Modifier.height(16.dp))

        // Muestra la fecha automática.
        OutlinedTextField(
            value = fecha,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Fecha del gasto")
            },
            readOnly = true,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

// Comprueba los datos y solicita el guardado.
        Button(
            onClick = {
                val error = validarGasto(cantidad, categoria, descripcion)

                if (error != null) {
                    mensaje = error
                } else {
                    mensaje = ""

                    val gasto = Gasto(
                        montoCentavos = cantidad.toBigDecimal()
                            .movePointRight(2)
                            .longValueExact(),
                        descripcion = descripcion.trim(),
                        categoria = categoria,
                        fechaMillis = fechaMillis
                    )

                    gastoViewModel.guardar(gasto)
                }
            },
            enabled = !gastoViewModel.guardando &&
                    !gastoViewModel.gastoGuardado,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3478C0)
            )
        ) {
            Text(
                text = if (gastoViewModel.guardando) {
                    "Guardando..."
                } else {
                    "Guardar gasto"
                },
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

// Muestra el resultado debajo del botón.
        if (mensaje.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = mensaje,
                fontSize = 14.sp,
                color = Color(0xFF263238)
            )
        }
        // Muestra un error si no se pudo guardar en SQLite.
        gastoViewModel.errorGuardado?.let { error ->
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = error,
                color = Color(0xFFB3261E),
                fontSize = 14.sp
            )
        }
    } // Cierra la Column principal.
} // Cierra PantallaNuevoGasto.