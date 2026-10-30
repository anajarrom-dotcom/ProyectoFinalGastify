package edu.umg.proyecto_gastify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import edu.umg.proyecto_gastify.ui.theme.Proyecto_GastifyTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModelProvider
import edu.umg.proyecto_gastify.datos.GastifyDatabase
import edu.umg.proyecto_gastify.datos.GastoRepositorio
import edu.umg.proyecto_gastify.viewmodel.GastoViewModel
import edu.umg.proyecto_gastify.viewmodel.GastoViewModelFactory
import java.math.BigDecimal
import edu.umg.proyecto_gastify.logica.calcularGastosDelMes
import edu.umg.proyecto_gastify.datos.IngresoRepositorio
import edu.umg.proyecto_gastify.viewmodel.IngresoViewModel
import edu.umg.proyecto_gastify.viewmodel.IngresoViewModelFactory
import edu.umg.proyecto_gastify.logica.calcularIngresosDelMes
import edu.umg.proyecto_gastify.logica.obtenerUltimosMovimientos

class MainActivity : ComponentActivity() { // Esta clase muestra la interfaz principal de la app
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtiene acceso a la base de datos local.
        val baseDatos = GastifyDatabase.obtenerInstancia(applicationContext)

// Entrega el DAO al repositorio.
        val repositorio = GastoRepositorio(baseDatos.gastoDao())

// Obtiene el ViewModel administrado por Android.
        val gastoViewModel = ViewModelProvider(
            this,
            GastoViewModelFactory(repositorio)
        )[GastoViewModel::class.java]

        // Conecta el repositorio y el ViewModel de ingresos.
        val ingresoRepositorio = IngresoRepositorio(baseDatos.ingresoDao())

        val ingresoViewModel = ViewModelProvider(
            this,
            IngresoViewModelFactory(ingresoRepositorio)
        )[IngresoViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            Proyecto_GastifyTheme {
                // Controla si mostramos Inicio o Registrar gasto.
                var mostrarNuevoGasto by rememberSaveable {
                    mutableStateOf(false)
                }

                var mostrarNuevoIngreso by rememberSaveable {
                    mutableStateOf(false)
                }

                // Regresa a Inicio al usar Atrás de Android.
                // Regresa a Inicio desde cualquiera de los formularios.
                BackHandler(
                    enabled = mostrarNuevoGasto || mostrarNuevoIngreso
                ) {
                    mostrarNuevoGasto = false
                    mostrarNuevoIngreso = false
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    when {
                        mostrarNuevoGasto -> {
                            PantallaNuevoGasto(
                                onVolver = {
                                    mostrarNuevoGasto = false
                                },
                                gastoViewModel = gastoViewModel,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        mostrarNuevoIngreso -> {
                            PantallaNuevoIngreso(
                                onVolver = {
                                    mostrarNuevoIngreso = false
                                },
                                ingresoViewModel = ingresoViewModel,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        else -> {
                            PantallaInicio(
                                name = "Tony",
                                onNuevoGasto = {
                                    gastoViewModel.prepararNuevoGasto()
                                    mostrarNuevoIngreso = false
                                    mostrarNuevoGasto = true
                                },
                                onNuevoIngreso = {
                                    ingresoViewModel.prepararNuevoIngreso()
                                    mostrarNuevoGasto = false
                                    mostrarNuevoIngreso = true
                                },
                                gastoViewModel = gastoViewModel,
                                ingresoViewModel = ingresoViewModel,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun PantallaInicio(
        name: String,
        onNuevoGasto: () -> Unit,
        onNuevoIngreso: () -> Unit,
        gastoViewModel: GastoViewModel,
        ingresoViewModel: IngresoViewModel,
        modifier: Modifier = Modifier
    ) {

        val totalGastosMes = calcularGastosDelMes(gastoViewModel.gastos)
        val totalIngresosMes = calcularIngresosDelMes(
            ingresoViewModel.ingresos
        )

        val saldoMes = totalIngresosMes.subtract(totalGastosMes)

        val cargandoResumen = gastoViewModel.cargandoGastos ||
                ingresoViewModel.cargandoIngresos

        val errorResumen = gastoViewModel.errorCarga != null ||
                ingresoViewModel.errorCarga != null

        val ultimosMovimientos = obtenerUltimosMovimientos(
            gastoViewModel.gastos,
            ingresoViewModel.ingresos
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            EncabezadoInicio(nombre = name)
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF142D4E)
                )
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Saldo del mes",
                        fontSize = 16.sp,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = when {
                            cargandoResumen -> "Cargando..."
                            errorResumen -> "No disponible"
                            else -> "Q${saldoMes.toPlainString()}"
                        },
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Ingresos menos gastos",
                        fontSize = 14.sp,
                        color = Color(0xFFCADCF0)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFEAF6EF)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Ingresos", color = Color(0xFF263238))

                        Text(
                            text = when {
                                ingresoViewModel.cargandoIngresos -> "Cargando..."
                                ingresoViewModel.errorCarga != null -> "No disponible"
                                else -> "Q${totalIngresosMes.toPlainString()}"
                            },
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E9D72)
                        )
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFCEEEF)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Gastos", color = Color(0xFF263238))

                        Text(
                            text = when {
                                gastoViewModel.cargandoGastos -> "Cargando..."
                                gastoViewModel.errorCarga != null -> "No disponible"
                                else -> "Q${totalGastosMes.toPlainString()}"
                            },
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE76F70)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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

                    Text(
                        text = "Sin configurar",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3478C0)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Establece cuánto quieres gastar este mes.",
                        fontSize = 14.sp,
                        color = Color(0xFF607080)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onNuevoGasto,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3478C0)
                    )
                ) {
                    Text(
                        text = "+ Nuevo gasto",
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                OutlinedButton(
                    onClick = onNuevoIngreso,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF3478C0)
                    )
                ) {
                    Text(
                        text = "+ Ingreso",
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            // Separa los botones de la sección de movimientos.
            Spacer(modifier = Modifier.height(24.dp))

// Tarjeta que mostrará los movimientos más recientes.
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                // Distribuye el título y el mensaje dentro de la tarjeta.
                Column(modifier = Modifier.padding(20.dp)) {

                    // Título de la sección.
                    Text(
                        text = "Últimos movimientos",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF142D4E)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Muestra el estado de la consulta o los últimos tres gastos.
                    // Muestra ingresos y gastos reales.
                    when {
                        cargandoResumen -> {
                            Text("Cargando movimientos...")
                        }

                        errorResumen -> {
                            Text(
                                text = "No se pudieron cargar los movimientos.",
                                color = Color(0xFFB3261E)
                            )
                        }

                        ultimosMovimientos.isEmpty() -> {
                            Text(
                                text = "Aún no hay movimientos",
                                color = Color(0xFF263238)
                            )
                        }

                        else -> {
                            ultimosMovimientos.forEach { movimiento ->
                                val monto = BigDecimal.valueOf(
                                    movimiento.montoCentavos,
                                    2
                                ).toPlainString()

                                val signo = if (movimiento.esIngreso) "+" else "-"
                                val colorMonto = if (movimiento.esIngreso) {
                                    Color(0xFF237A57)
                                } else {
                                    Color(0xFFB3261E)
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = movimiento.descripcion,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF263238)
                                        )

                                        Text(
                                            text = movimiento.detalle,
                                            fontSize = 14.sp,
                                            color = Color(0xFF607080)
                                        )
                                    }

                                    Text(
                                        text = "${signo}Q$monto",
                                        fontWeight = FontWeight.Bold,
                                        color = colorMonto
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Separa los botones de la tarjeta de inspiración.
            Spacer(modifier = Modifier.height(24.dp))

// Tarjeta de inspiración con una frase de San Carlo Acutis.
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE5F1FC)
                )
            ) {
                // Organiza el título, la frase y el autor verticalmente.
                Column(modifier = Modifier.padding(20.dp)) {

                    // Título de la sección.
                    Text(
                        text = "Inspiración del dia",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF142D4E)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Frase fija mientras implementamos la selección de frases.
                    Text(
                        text = "«La Eucaristía es mi autopista hacia el Cielo».",
                        fontSize = 16.sp,
                        color = Color(0xFF263238)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Nombre del autor.
                    // Nombre fijo del autor de las frases.
                    Text(
                        text = "San Carlo Acutis",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3478C0)
                    )

// Subtítulo fijo debajo del nombre.
                    Text(
                        text = "Ciberapóstol de Dios",
                        fontSize = 12.sp,
                        color = Color(0xFF607080)
                    )
                }
            }
        }
    }

    // Muestra el nombre de la aplicación y el saludo.
    @Composable
    fun EncabezadoInicio(nombre: String) {
        Text(
            text = "Gastify",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF142D4E)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Hola, $nombre 👋",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF142D4E)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tu dinero bajo control",
            fontSize = 16.sp,
            color = Color(0xFF607080)
        )
    }
}