package es.gaspardev.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.gaspardev.layout.settings.AccountSettings
import es.gaspardev.layout.settings.AppearanceSettings
import es.gaspardev.layout.settings.NotificationSettings
import es.gaspardev.layout.settings.ProfileSettings

@Composable
fun SettingsScreen() {
    var activeTab by remember { mutableStateOf("profile") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(bottom = 16.dp)) {
            Text(
                text = "Configuración",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Administra tu cuenta y preferencias de la aplicación",
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
        }

        ScrollableTabRow(
            selectedTabIndex = when (activeTab) {
                "profile" -> 0
                "account" -> 1
                "appearance" -> 2
                "notifications" -> 3
                else -> 0
            },
            edgePadding = 0.dp,
            backgroundColor = Color.Transparent,
            contentColor = MaterialTheme.colors.primary
        ) {
            listOf("Perfil", "Cuenta", "Apariencia", "Notificaciones")
                .forEachIndexed { index, title ->
                    Tab(
                        selected = when (index) {
                            0 -> activeTab == "profile"
                            1 -> activeTab == "account"
                            2 -> activeTab == "appearance"
                            3 -> activeTab == "notifications"
                            else -> false
                        },
                        onClick = {
                            activeTab = when (index) {
                                0 -> "profile"
                                1 -> "account"
                                2 -> "appearance"
                                3 -> "notifications"
                                else -> "profile"
                            }
                        },
                        text = { Text(title) }
                    )
                }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when (activeTab) {
                "profile" -> ProfileSettings()
                "account" -> AccountSettings()
                "appearance" -> AppearanceSettings()
                "notifications" -> NotificationSettings()
            }
        }
    }
}
