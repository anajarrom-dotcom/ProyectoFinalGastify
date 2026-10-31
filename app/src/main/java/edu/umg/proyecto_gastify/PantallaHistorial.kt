package edu.umg.proyecto_gastify

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.umg.proyecto_gastify.modelo.MovimientoResumen
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PantallaHistorial(
    movimientos: List<MovimientoResumen>,
    cargando: Boolean,
    error: String?,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formatoFecha = remember {
        SimpleDateFormat("dd/MM/yyyy · HH:mm", Locale("es", "GT"))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .padding(24.dp)
    ) {
        TextButton(onClick = onVolver) {
            Text(
                text = "← Volver",
                color = Color(0xFF218563)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Tus movimientos",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF142D4E)
        )

        Text(
            text = "Historial de ingresos y gastos",
            color = Color(0xFF607080)
        )

        Spacer(modifier = Modifier.height(20.dp))

        when {
            cargando -> {
                Text("Cargando movimientos...")
            }

            error != null -> {
                Text(
                    text = error,
                    color = Color(0xFFB3261E)
                )
            }

            movimientos.isEmpty() -> {
                Text("Todavía no tienes movimientos registrados.")
            }

            else -> {
                // Lista desplazable que crea los elementos según se necesitan.
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(movimientos) { movimiento ->
                        val monto = BigDecimal.valueOf(
                            movimiento.montoCentavos,
                            2
                        ).toPlainString()

                        val colorMonto = if (movimiento.esIngreso) {
                            Color(0xFF237A57)
                        } else {
                            Color(0xFFB3261E)
                        }

                        val tipo = if (movimiento.esIngreso) {
                            "Ingreso"
                        } else {
                            "Gasto"
                        }

                        val signo = if (movimiento.esIngreso) "+" else "-"

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(18.dp)
                            ) {
                                Text(
                                    text = tipo,
                                    color = colorMonto,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = movimiento.descripcion,
                                    color = Color(0xFF142D4E),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium
                                )

                                Text(
                                    text = movimiento.detalle,
                                    color = Color(0xFF607080)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "${signo}Q$monto",
                                    color = colorMonto,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = formatoFecha.format(
                                        Date(movimiento.fechaMillis)
                                    ),
                                    color = Color(0xFF607080),
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
