package edu.umg.proyecto_gastify

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Solicita el nombre antes de entrar a Inicio.
@Composable
fun PantallaBienvenida(
    onComenzar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var mostrarError by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFEAF3FC))
            .clipToBounds()
    ) {
        // Círculos decorativos del fondo.
        Box(
            modifier = Modifier
                .size(260.dp)
                .offset(x = (-100).dp, y = (-90).dp)
                .background(Color(0xFFD0EEE3), CircleShape)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(240.dp)
                .offset(x = 100.dp, y = 80.dp)
                .background(Color(0xFFD0EEE3), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Identidad de Gastify.
            Text(
                text = "Gastify",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF218563)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tu dinero bajo control",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF142D4E),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Tarjeta para ingresar el nombre.
            Card(
                modifier = Modifier
                    .widthIn(max = 460.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "¡Bienvenido!",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF142D4E)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Comencemos a organizar tu dinero.",
                        color = Color(0xFF607080),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = {
                            if (it.length <= 40) {
                                nombre = it
                                mostrarError = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("¿Cómo te llamas?") },
                        singleLine = true,
                        isError = mostrarError,
                        supportingText = {
                            if (mostrarError) {
                                Text("Escribe tu nombre para continuar.")
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Valida y entrega el nombre a la aplicación.
                    Button(
                        onClick = {
                            val nombreLimpio = nombre.trim()

                            if (nombreLimpio.isBlank()) {
                                mostrarError = true
                            } else {
                                onComenzar(nombreLimpio)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 54.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF218563),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Comenzar",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Tus datos se guardan en este teléfono",
                fontSize = 13.sp,
                color = Color(0xFF607080),
                textAlign = TextAlign.Center
            )
        }
    }
}
