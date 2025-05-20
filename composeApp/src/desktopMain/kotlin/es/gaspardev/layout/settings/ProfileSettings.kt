package es.gaspardev.layout.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileSettings() {
    var name by remember { mutableStateOf("Alex Trainer") }
    var email by remember { mutableStateOf("alex@fit-me.com") }
    var bio by remember { mutableStateOf("Entrenador personal certificado con más de 5 años de experiencia...") }
    var phone by remember { mutableStateOf("+1 (555) 123-4567") }
    var specialization by remember { mutableStateOf("strength") }
    var certifications by remember { mutableStateOf("NSCA-CPT, ACE, NASM") }
    var website by remember { mutableStateOf("https://alextrainer.com") }
    var location by remember { mutableStateOf("Madrid, España") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp)
    ) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Información del Perfil",
                    style = MaterialTheme.typography.h3
                )
                Text(
                    text = "Actualiza tu información personal y profesional",
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        modifier = Modifier.width(120.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Avatar would go here
                        Button(
                            onClick = { /* Handle photo change */ },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Icon(Icons.Default.AccountBox, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Cambiar foto")
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        // Form fields would go here
                        // Similar structure to the AccountSettings implementation
                    }
                }

                Button(
                    onClick = { isLoading = true },
                    modifier = Modifier.align(Alignment.End),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = MaterialTheme.colors.onPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Text("Guardar cambios")
                }
            }
        }
    }
}