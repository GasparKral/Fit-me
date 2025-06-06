package es.gaspardev.ui.screens.athlete

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import es.gaspardev.icons.FitMeIcons

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

@OptIn(ExperimentalMaterial3Api::class)
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                // Header con saludo y perfil
                DashboardHeader(onNavigateToProfile = onNavigateToProfile)
            }

            item {
                // Resumen de progreso diario
                DailyProgressCard()
            }

            item {
                // Próximo entrenamiento
                NextWorkoutCard(onNavigateToWorkout = onNavigateToWorkout)
            }

            item {
                // Accesos rápidos
                QuickAccessSection(
                    onNavigateToNutrition = onNavigateToNutrition,
                    onNavigateToProgress = onNavigateToProgress,
                    onNavigateToChat = onNavigateToChat
                )
            }

            item {
                // Estadísticas semanales
                WeeklyStatsCard()
            }

            item {
                // Mensajes del entrenador
                TrainerMessagesCard(onNavigateToChat = onNavigateToChat)
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
        Column {
            Text(
                text = "¡Hola, Alex! 👋",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "¿Listo para entrenar hoy?",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }

        // Avatar del usuario
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable { onNavigateToProfile() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = "Perfil",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun DailyProgressCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Progreso de Hoy",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DailyProgressItem(
                    title = "Entrenamiento",
                    progress = 0.75f,
                    label = "45/60 min"
                )
                DailyProgressItem(
                    title = "Nutrición",
                    progress = 0.6f,
                    label = "3/5 comidas"
                )
                DailyProgressItem(
                    title = "Hidratación",
                    progress = 0.8f,
                    label = "2.4/3L"
                )
            }
        }
    }
}

@Composable
private fun DailyProgressItem(
    title: String,
    progress: Float,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(60.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f),
                strokeWidth = 4.dp
            )
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
        )
    }
}

@Composable
private fun NextWorkoutCard(
    onNavigateToWorkout: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigateToWorkout() },
        shape = MaterialTheme.shapes.medium
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
                    text = "Próximo Entrenamiento",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )

                Icon(
                    FitMeIcons.Weight,
                    contentDescription = "Entrenamiento",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Entrenamiento de Fuerza - Tren Superior",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    FitMeIcons.Calendar,
                    contentDescription = "Duración",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "45 minutos • 6 ejercicios",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNavigateToWorkout,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "COMENZAR AHORA",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary
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
            horizontalArrangement = Arrangement.spacedBy(12.dp)
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
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(100.dp)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium
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
                contentDescription = title,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun WeeklyStatsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Esta Semana",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    value = "4",
                    label = "Entrenamientos",
                    icon = FitMeIcons.Weight
                )
                StatItem(
                    value = "2.1kg",
                    label = "Progreso",
                    icon = Icons.Default.ThumbUp
                )
                StatItem(
                    value = "92%",
                    label = "Adherencia",
                    icon = Icons.Default.CheckCircle
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    icon: ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun TrainerMessagesCard(
    onNavigateToChat: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigateToChat() },
        shape = MaterialTheme.shapes.medium
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
                    fontWeight = FontWeight.SemiBold
                )

                Badge(
                    containerColor = MaterialTheme.colorScheme.secondary
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
                maxLines = 2
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
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Inicio"
                )
            },
            label = { Text("Inicio") },
            selected = selectedIndex == 0,
            onClick = { /* Ya estamos en dashboard */ }
        )

        NavigationBarItem(
            icon = {
                Icon(
                    FitMeIcons.Weight,
                    contentDescription = "Entrenamientos"
                )
            },
            label = { Text("Entrenar") },
            selected = selectedIndex == 1,
            onClick = onWorkoutClick
        )

        NavigationBarItem(
            icon = {
                Icon(
                    FitMeIcons.Nutrition,
                    contentDescription = "Nutrición"
                )
            },
            label = { Text("Nutrición") },
            selected = selectedIndex == 2,
            onClick = onNutritionClick
        )

        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.ThumbUp,
                    contentDescription = "Progreso"
                )
            },
            label = { Text("Progreso") },
            selected = selectedIndex == 3,
            onClick = onProgressClick
        )

        NavigationBarItem(
            icon = {
                Icon(
                    FitMeIcons.Messages,
                    contentDescription = "Mensajes"
                )
            },
            label = { Text("Chat") },
            selected = selectedIndex == 4,
            onClick = onChatClick
        )
    }
}

// Data classes para organizar la información
private data class QuickAccessItem(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)