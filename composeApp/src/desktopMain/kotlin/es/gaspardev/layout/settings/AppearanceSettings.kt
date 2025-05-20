package es.gaspardev.layout.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppearanceSettings() {
    var theme by remember { mutableStateOf("system") }
    var language by remember { mutableStateOf("es") }
    var measurementUnit by remember { mutableStateOf("metric") }
    var dateFormat by remember { mutableStateOf("dd/mm/yyyy") }
    var compactMode by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Tema",
                    style = MaterialTheme.typography.h3
                )
                Text(
                    text = "Personaliza la apariencia de la aplicación",
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Theme selection cards would go here
                }

                Divider(modifier = Modifier.padding(vertical = 16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Modo compacto")
                        Text(
                            text = "Reduce el espaciado y tamaño de los elementos para mostrar más contenido",
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                            fontSize = 14.sp
                        )
                    }
                    Switch(
                        checked = compactMode,
                        onCheckedChange = { compactMode = it }
                    )
                }
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Preferencias regionales",
                    style = MaterialTheme.typography.h3
                )
                Text(
                    text = "Configura el idioma y formato de visualización",
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Idioma")
                        // Language dropdown would go here
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Formato de fecha")
                        // Date format dropdown would go here
                    }
                }

                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Text("Unidades de medida")
                    // Measurement unit radio buttons would go here
                }
            }
        }
    }
}