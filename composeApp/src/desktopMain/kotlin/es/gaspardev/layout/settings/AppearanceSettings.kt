package es.gaspardev.layout.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.gaspardev.ThemeOption.selectedTheme
import es.gaspardev.ThemeOption.modifyTheme

@Composable
fun AppearanceSettings() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Tema", style = MaterialTheme.typography.h3)
                Text(
                    "Personaliza la apariencia de la aplicación",
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("system" to "Sistema", "light" to "Claro", "dark" to "Oscuro").forEach { (value, label) ->
                        ThemeOption(label, value, selectedTheme) {
                            modifyTheme(value)
                        }
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 16.dp))
            }
        }
    }
}


@Composable
fun ThemeOption(
    label: String,
    value: String,
    selectedValue: String,
    onSelected: () -> Unit
) {
    OutlinedButton(
        onClick = onSelected,
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = if (value == selectedValue)
                MaterialTheme.colors.primary.copy(alpha = 0.2f)
            else MaterialTheme.colors.surface
        )
    ) {
        Text(text = label, fontSize = 14.sp)
    }
}