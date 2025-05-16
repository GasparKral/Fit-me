package es.gaspardev.layout.Settings

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
fun AccountSettings() {
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
                    text = "Cambiar contraseña",
                    style = MaterialTheme.typography.h3
                )
                Text(
                    text = "Actualiza tu contraseña para mantener tu cuenta segura",
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Password form would go here
                // Similar to the ProfileSettings implementation
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Seguridad de la cuenta",
                    style = MaterialTheme.typography.h3
                )
                Text(
                    text = "Configura opciones adicionales de seguridad para proteger tu cuenta",
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                var twoFactorEnabled by remember { mutableStateOf(false) }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Autenticación de dos factores")
                        Text(
                            text = "Añade una capa adicional de seguridad a tu cuenta",
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                            fontSize = 14.sp
                        )
                    }
                    Switch(
                        checked = twoFactorEnabled,
                        onCheckedChange = { twoFactorEnabled = it }
                    )
                }

                if (twoFactorEnabled) {
                    AlertDialog(
                        onDismissRequest = { /* Handle dismiss */ },
                        title = { Text("Configuración requerida") },
                        text = {
                            Text("Para completar la configuración de la autenticación de dos factores, necesitas escanear un código QR con tu aplicación de autenticación.")
                        },
                        confirmButton = {
                            Button(onClick = { /* Handle setup */ }) {
                                Text("Configurar ahora")
                            }
                        },
                        dismissButton = {
                            Button(onClick = { twoFactorEnabled = false }) {
                                Text("Cerrar")
                            }
                        }
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Notificaciones de inicio de sesión")
                        Text(
                            text = "Recibe notificaciones cuando alguien inicie sesión en tu cuenta",
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                            fontSize = 14.sp
                        )
                    }
                    Switch(checked = true, onCheckedChange = {})
                }

                Divider(modifier = Modifier.padding(vertical = 16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Sesiones activas")
                        Text(
                            text = "Administra los dispositivos donde has iniciado sesión",
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                            fontSize = 14.sp
                        )
                    }
                    Button(onClick = { /* Handle manage sessions */ }) {
                        Text("Administrar sesiones")
                    }
                }
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Eliminar cuenta",
                    style = MaterialTheme.typography.h3
                )
                Text(
                    text = "Elimina permanentemente tu cuenta y todos tus datos",
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

            }
        }
    }
}