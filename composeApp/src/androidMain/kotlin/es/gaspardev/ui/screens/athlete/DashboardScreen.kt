package es.gaspardev.ui.screens.athlete

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.ui.states.LoggedAthlete
import kotlinx.coroutines.delay


@Destination
@Composable
fun DashboardScreen(
    navigator: DestinationsNavigator
) {
    DashboardScreenContent(
        /* onNavigateToWorkout = {
             navigator.navigate(WorkoutScreenDestination)
         },
         onNavigateToNutrition = {
             navigator.navigate(NutritionScreenDestination)
         },
         onNavigateToProgress = {
             navigator.navigate(ProgressScreenDestination)
         },
         onNavigateToChat = {
             navigator.navigate(ChatScreenDestination)
         },
         onNavigateToProfile = {
             navigator.navigate(ProfileScreenDestination)
         }*/
    )
}

@Composable
fun DashboardScreenContent(
    onNavigateToWorkout: () -> Unit = {},
    onNavigateToNutrition: () -> Unit = {},
    onNavigateToProgress: () -> Unit = {},
    onNavigateToChat: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    val selectedBottomIndex = remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = selectedBottomIndex.value,
                onWorkoutClick = {
                    selectedBottomIndex.value = 1
                    onNavigateToWorkout()
                },
                onNutritionClick = {
                    selectedBottomIndex.value = 2
                    onNavigateToNutrition()
                },
                onProgressClick = {
                    selectedBottomIndex.value = 3
                    onNavigateToProgress()
                },
                onChatClick = {
                    selectedBottomIndex.value = 4
                    onNavigateToChat()
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DashboardHeader { onNavigateToProfile() }

            WorkoutControls()

            QuickAccessSection(
                onNavigateToNutrition = onNavigateToNutrition,
                onNavigateToProgress = onNavigateToProgress,
                onNavigateToChat = onNavigateToChat
            )

            TrainerMessagesCard(onNavigateToChat = onNavigateToChat)

            // Espaciado adicional para el bottom navigation
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun WorkoutControls() {
    var repCount by remember { mutableStateOf(0) }
    var timeInSeconds by remember { mutableStateOf(0) }
    var isTimerRunning by remember { mutableStateOf(false) }

    // Cronómetro
    LaunchedEffect(isTimerRunning) {
        if (isTimerRunning) {
            while (isTimerRunning) {
                delay(1000)
                timeInSeconds++
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = "Controles de entrenamiento"
            },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Controles de Entrenamiento",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            // Layout adaptativo para móvil
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Cronómetro
                Timer(
                    timeInSeconds = timeInSeconds,
                    isRunning = isTimerRunning,
                    onStart = { isTimerRunning = true },
                    onPause = { isTimerRunning = false },
                    onReset = {
                        isTimerRunning = false
                        timeInSeconds = 0
                    }
                )
                // Contador de Repeticiones
                RepetitionCounter(
                    count = repCount,
                    onIncrement = { repCount++ },
                    onDecrement = { if (repCount > 0) repCount-- },
                    onReset = { repCount = 0 }
                )
            }
        }
    }
}

@Composable
private fun RepetitionCounter(
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onReset: () -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Repeticiones",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledIconButton(
                    onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        onDecrement()
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .semantics {
                            contentDescription = "Reducir repeticiones"
                        },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    enabled = count > 0
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 42.sp,
                    modifier = Modifier.semantics {
                        liveRegion = LiveRegionMode.Polite
                        contentDescription = "$count repeticiones"
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                FilledIconButton(
                    onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        onIncrement()
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .semantics {
                            contentDescription = "Aumentar repeticiones"
                        },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            TextButton(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    onReset()
                },
                modifier = Modifier.semantics {
                    contentDescription = "Reiniciar contador de repeticiones"
                }
            ) {
                Text("Reiniciar")
            }
        }
    }
}

@Composable
private fun Timer(
    timeInSeconds: Int,
    isRunning: Boolean,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current
    val minutes = timeInSeconds / 60
    val seconds = timeInSeconds % 60
    val timeText = String.format("%02d:%02d", minutes, seconds)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Cronómetro",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = timeText,
                style = MaterialTheme.typography.displayMedium,
                color = if (isRunning) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                modifier = Modifier.semantics {
                    liveRegion = LiveRegionMode.Polite
                    contentDescription = "Tiempo transcurrido: $minutes minutos y $seconds segundos"
                }
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                FilledIconButton(
                    onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        onReset()
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .semantics {
                            contentDescription = "Reiniciar cronómetro"
                        },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                FilledIconButton(
                    onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        if (isRunning) onPause() else onStart()
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .semantics {
                            contentDescription =
                                if (isRunning) "Pausar cronómetro" else "Iniciar cronómetro"
                        },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = if (isRunning) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        if (isRunning) Icons.Default.Stop else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }

            }
        }
    }
}

@Composable
private fun DashboardHeader(
    onNavigateToProfile: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "¡Hola, ${LoggedAthlete.state.athlete.user.fullname}! 👋",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "¿Listo para entrenar hoy?",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Avatar del usuario con mejor accesibilidad
        Surface(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable(
                    onClickLabel = "Ir al perfil"
                ) { onNavigateToProfile() }
                .semantics {
                    role = Role.Button
                    contentDescription = "Botón de perfil de usuario"
                },
            color = MaterialTheme.colorScheme.primary,
            tonalElevation = 2.dp
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
private fun QuickAccessSection(
    onNavigateToNutrition: () -> Unit,
    onNavigateToProgress: () -> Unit,
    onNavigateToChat: () -> Unit
) {
    Column {
        Text(
            text = "Accesos Rápidos",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp),
            modifier = Modifier.semantics {
                contentDescription = "Lista de accesos rápidos"
            }
        ) {
            items(
                listOf(
                    QuickAccessItem("Dieta", FitMeIcons.Nutrition, onNavigateToNutrition),
                    QuickAccessItem("Progreso", Icons.Default.ThumbUp, onNavigateToProgress),
                    QuickAccessItem("Mensajes", FitMeIcons.Messages, onNavigateToChat)
                )
            ) { item ->
                QuickAccessCard(
                    title = item.title,
                    icon = item.icon,
                    onClick = item.onClick
                )
            }
        }
    }
}

@Composable
private fun QuickAccessCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .clickable(
                onClickLabel = "Ir a $title"
            ) {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            }
            .semantics {
                role = Role.Button
                contentDescription = "Acceso rápido a $title"
            },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun TrainerMessagesCard(
    onNavigateToChat: () -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClickLabel = "Ver mensajes del entrenador"
            ) {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                onNavigateToChat()
            }
            .semantics {
                role = Role.Button
                contentDescription = "Tarjeta de mensajes del entrenador. 2 mensajes nuevos"
            },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mensajes del Entrenador",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.width(8.dp))

                Badge(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.semantics {
                        contentDescription = "2 mensajes nuevos"
                    }
                ) {
                    Text(
                        text = "2",
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Carlos: ¡Excelente progreso esta semana! Mañana...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Hace 2 horas",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun BottomNavigationBar(
    selectedIndex: Int,
    onWorkoutClick: () -> Unit,
    onNutritionClick: () -> Unit,
    onProgressClick: () -> Unit,
    onChatClick: () -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        modifier = Modifier.semantics {
            contentDescription = "Barra de navegación principal"
        }
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = null
                )
            },
            label = { Text("Inicio") },
            selected = selectedIndex == 0,
            onClick = { /* Ya estamos en dashboard */ },
            modifier = Modifier.semantics {
                contentDescription = "Pestaña de inicio, seleccionada"
            }
        )

        NavigationBarItem(
            icon = {
                Icon(
                    FitMeIcons.Weight,
                    contentDescription = null
                )
            },
            label = { Text("Entrenar") },
            selected = selectedIndex == 1,
            onClick = onWorkoutClick,
            modifier = Modifier.semantics {
                contentDescription =
                    if (selectedIndex == 1) "Pestaña de entrenar, seleccionada" else "Pestaña de entrenar"
            }
        )

        NavigationBarItem(
            icon = {
                Icon(
                    FitMeIcons.Nutrition,
                    contentDescription = null
                )
            },
            label = { Text("Nutrición") },
            selected = selectedIndex == 2,
            onClick = onNutritionClick,
            modifier = Modifier.semantics {
                contentDescription =
                    if (selectedIndex == 2) "Pestaña de nutrición, seleccionada" else "Pestaña de nutrición"
            }
        )

        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.ThumbUp,
                    contentDescription = null
                )
            },
            label = { Text("Progreso") },
            selected = selectedIndex == 3,
            onClick = onProgressClick,
            modifier = Modifier.semantics {
                contentDescription =
                    if (selectedIndex == 3) "Pestaña de progreso, seleccionada" else "Pestaña de progreso"
            }
        )

        NavigationBarItem(
            icon = {
                Icon(
                    FitMeIcons.Messages,
                    contentDescription = null
                )
            },
            label = { Text("Chat") },
            selected = selectedIndex == 4,
            onClick = onChatClick,
            modifier = Modifier.semantics {
                contentDescription =
                    if (selectedIndex == 4) "Pestaña de chat, seleccionada" else "Pestaña de chat"
            }
        )
    }
}

// Data classes para organizar la información
private data class QuickAccessItem(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)