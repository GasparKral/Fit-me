package es.gaspardev.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import es.gaspardev.AppTheme
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.ui.screens.destinations.DashboardScreenDestination
import es.gaspardev.ui.screens.destinations.LoginScreenDestination

@Destination
@Composable
fun ConnectionScreenDestination(
    navigator: DestinationsNavigator
) {
    ConnectionScreenContent(
        onConnectionSuccess = {
            navigator.navigate(DashboardScreenDestination) {
                popUpTo(LoginScreenDestination.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        },
        onNavigateBack = {
            navigator.navigateUp()
        }
    )
}

@Destination
@Composable
fun ConnectionScreenContent(
    onConnectionSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var trainerCode by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Top Bar
                TopAppBar(
                    title = { Text("Conectar con Entrenador") },
                    navigationIcon = {
                        IconButton(onClick = { onConnectionSuccess() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                        }
                    },
                    backgroundColor = MaterialTheme.colors.surface,
                    elevation = 4.dp
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Spacer(modifier = Modifier.height(32.dp))

                    // Título y descripción
                    Text(
                        text = "¿Cómo quieres conectarte?",
                        style = MaterialTheme.typography.h2,
                        color = MaterialTheme.colors.onBackground,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Elige una de las siguientes opciones para conectarte con tu entrenador",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Opción 1: Escanear QR
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        elevation = 4.dp,
                        shape = MaterialTheme.shapes.medium,
                        backgroundColor = MaterialTheme.colors.surface
                    ) {
                        Button(
                            onClick = {

                                isLoading = true
                                // Simular éxito después de un delay
                                onConnectionSuccess()
                            },
                            modifier = Modifier.fillMaxSize(),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Transparent
                            ),
                            elevation = ButtonDefaults.elevation(0.dp),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    FitMeIcons.Weight,
                                    contentDescription = "Escanear QR",
                                    modifier = Modifier.size(40.dp),
                                    tint = MaterialTheme.colors.primary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Escanear código QR",
                                    style = MaterialTheme.typography.h3,
                                    color = MaterialTheme.colors.onSurface
                                )
                                Text(
                                    text = "Tu entrenador te mostrará un código",
                                    style = MaterialTheme.typography.caption,
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }

                    // Opción 2: Enlace directo
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        elevation = 4.dp,
                        shape = MaterialTheme.shapes.medium,
                        backgroundColor = MaterialTheme.colors.surface
                    ) {
                        Button(
                            onClick = {

                                isLoading = true
                                onConnectionSuccess()
                            },
                            modifier = Modifier.fillMaxSize(),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Transparent
                            ),
                            elevation = ButtonDefaults.elevation(0.dp),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    FitMeIcons.Weight,
                                    contentDescription = "Enlace directo",
                                    modifier = Modifier.size(40.dp),
                                    tint = MaterialTheme.colors.secondary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Enlace directo",
                                    style = MaterialTheme.typography.h3,
                                    color = MaterialTheme.colors.onSurface
                                )
                                Text(
                                    text = "Si tienes un enlace de invitación",
                                    style = MaterialTheme.typography.caption,
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }

                    // Opción 3: Código de entrenador
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 4.dp,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Código de entrenador",
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colors.primary
                            )

                            Text(
                                text = "Código de entrenador",
                                style = MaterialTheme.typography.h3,
                                color = MaterialTheme.colors.onSurface
                            )

                            OutlinedTextField(
                                value = trainerCode,
                                onValueChange = {
                                    trainerCode = it.uppercase()
                                    errorMessage = ""
                                },
                                label = { Text("Ingresa el código") },
                                placeholder = { Text("Ej: TR12345") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = MaterialTheme.colors.primary,
                                    focusedLabelColor = MaterialTheme.colors.primary
                                ),
                                singleLine = true
                            )

                            if (errorMessage.isNotEmpty()) {
                                Text(
                                    text = errorMessage,
                                    color = MaterialTheme.colors.error,
                                    style = MaterialTheme.typography.caption
                                )
                            }

                            Button(
                                onClick = {
                                    if (trainerCode.isNotEmpty()) {
                                        isLoading = true

                                        onConnectionSuccess()
                                    } else {
                                        errorMessage = "Por favor ingresa un código"
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                enabled = !isLoading,
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = MaterialTheme.colors.primary
                                ),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colors.onPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else {
                                    Text(
                                        text = "CONECTAR",
                                        style = MaterialTheme.typography.button,
                                        color = MaterialTheme.colors.onPrimary
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Ayuda
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = MaterialTheme.colors.secondary.copy(alpha = 0.1f),
                        elevation = 0.dp,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = "💡 Tu entrenador debe proporcionarte uno de estos métodos de conexión. Si tienes problemas, contacta directamente con él.",
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onBackground.copy(alpha = 0.8f),
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
