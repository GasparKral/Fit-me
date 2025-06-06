package es.gaspardev.ui.screens.athlete

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.gaspardev.AppTheme
import es.gaspardev.icons.FitMeIcons

/**
 * Pantalla de entrenamientos para deportistas
 * Muestra rutinas asignadas, progreso y herramientas de entrenamiento
 */
@Composable
fun WorkoutScreen(
    onNavigateToWorkoutDetail: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Rutinas", "Historial", "Herramientas")

    AppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Entrenamientos") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    },
                    backgroundColor = MaterialTheme.colors.surface,
                    elevation = 4.dp
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colors.background)
            ) {
                // Tabs
                TabRow(
                    selectedTabIndex = selectedTab,
                    backgroundColor = MaterialTheme.colors.surface,
                    contentColor = MaterialTheme.colors.primary
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    title,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }

                // Contenido según tab seleccionado
                when (selectedTab) {
                    0 -> WorkoutRoutinesTab(onNavigateToWorkoutDetail)
                    1 -> WorkoutHistoryTab()
                    2 -> WorkoutToolsTab()
                }
            }
        }
    }
}

@Composable
private fun WorkoutRoutinesTab(
    onNavigateToWorkoutDetail: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Rutina actual destacada
            CurrentWorkoutCard(onNavigateToWorkoutDetail)
        }

        item {
            Text(
                text = "Todas las Rutinas",
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(
            listOf(
                WorkoutItem("1", "Fuerza - Tren Superior", "45 min", "6 ejercicios", 0.8f, true),
                WorkoutItem("2", "Cardio HIIT", "30 min", "8 ejercicios", 1.0f, false),
                WorkoutItem("3", "Fuerza - Tren Inferior", "50 min", "7 ejercicios", 0.6f, false),
                WorkoutItem("4", "Flexibilidad y Movilidad", "25 min", "10 ejercicios", 0.9f, false)
            )
        ) { workout ->
            WorkoutRoutineCard(
                workout = workout,
                onNavigateToDetail = { onNavigateToWorkoutDetail(workout.id) }
            )
        }
    }
}

@Composable
private fun CurrentWorkoutCard(
    onNavigateToWorkoutDetail: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigateToWorkoutDetail("current") },
        elevation = 8.dp,
        shape = MaterialTheme.shapes.medium,
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Entrenamiento de Hoy",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onPrimary.copy(alpha = 0.8f)
                    )

                    Text(
                        text = "Fuerza - Tren Superior",
                        style = MaterialTheme.typography.h2,
                        color = MaterialTheme.colors.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Badge(
                    backgroundColor = MaterialTheme.colors.secondary,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = "ACTUAL",
                        color = MaterialTheme.colors.onSecondary,
                        style = MaterialTheme.typography.caption,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    FitMeIcons.Calendar,
                    contentDescription = "Duración",
                    tint = MaterialTheme.colors.onPrimary.copy(alpha = 0.8f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "45 minutos",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onPrimary.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Icon(
                    FitMeIcons.Weight,
                    contentDescription = "Ejercicios",
                    tint = MaterialTheme.colors.onPrimary.copy(alpha = 0.8f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "6 ejercicios",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onPrimary.copy(alpha = 0.8f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Progreso del entrenamiento
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Progreso: 3/6 ejercicios",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onPrimary.copy(alpha = 0.9f)
                    )

                    Text(
                        text = "50%",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = 0.5f,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colors.onPrimary,
                    backgroundColor = MaterialTheme.colors.onPrimary.copy(alpha = 0.3f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { onNavigateToWorkoutDetail("current") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.onPrimary,
                    contentColor = MaterialTheme.colors.primary
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "CONTINUAR ENTRENAMIENTO",
                    style = MaterialTheme.typography.button,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun WorkoutRoutineCard(
    workout: WorkoutItem,
    onNavigateToDetail: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigateToDetail() },
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indicador de progreso circular
            Box(
                modifier = Modifier.size(50.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = workout.progress,
                    modifier = Modifier.fillMaxSize(),
                    strokeWidth = 4.dp,
                    color = if (workout.isActive) MaterialTheme.colors.primary else MaterialTheme.colors.secondary,
                    backgroundColor = Color.Gray.copy(alpha = 0.3f)
                )

                Text(
                    text = "${(workout.progress * 100).toInt()}%",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = workout.title,
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onSurface,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )

                    if (workout.isActive) {
                        Badge(backgroundColor = MaterialTheme.colors.primary) {
                            Text(
                                text = "ACTIVA",
                                color = MaterialTheme.colors.onPrimary,
                                style = MaterialTheme.typography.caption
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        FitMeIcons.Calendar,
                        contentDescription = "Duración",
                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = workout.duration,
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Icon(
                        FitMeIcons.Weight,
                        contentDescription = "Ejercicios",
                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = workout.exercises,
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Icon(
                Icons.Default.MoreVert,
                contentDescription = "Ver detalles",
                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.4f),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun WorkoutHistoryTab() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Historial de Entrenamientos",
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(
            listOf(
                HistoryItem("Hoy", "Fuerza - Tren Superior", "45 min", "Completado", true),
                HistoryItem("Ayer", "Cardio HIIT", "30 min", "Completado", true),
                HistoryItem("Hace 2 días", "Fuerza - Tren Inferior", "50 min", "Completado", true),
                HistoryItem("Hace 3 días", "Flexibilidad", "25 min", "Saltado", false),
                HistoryItem("Hace 4 días", "Cardio Moderado", "40 min", "Completado", true)
            )
        ) { item ->
            WorkoutHistoryCard(item)
        }
    }
}

@Composable
private fun WorkoutHistoryCard(item: HistoryItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indicador de estado
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (item.completed) MaterialTheme.colors.primary.copy(alpha = 0.1f)
                        else MaterialTheme.colors.error.copy(alpha = 0.1f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    if (item.completed) Icons.Default.CheckCircle else Icons.Default.Warning,
                    contentDescription = item.status,
                    tint = if (item.completed) MaterialTheme.colors.primary else MaterialTheme.colors.error,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.workoutName,
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = item.date,
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                )

                Text(
                    text = "${item.duration} • ${item.status}",
                    style = MaterialTheme.typography.caption,
                    color = if (item.completed) MaterialTheme.colors.primary else MaterialTheme.colors.error
                )
            }
        }
    }
}

@Composable
private fun WorkoutToolsTab() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Herramientas de Entrenamiento",
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(
            listOf(
                ToolItem("Cronómetro", "Para descansos y series", FitMeIcons.Calendar, Color(0xFF4CAF50)),
                ToolItem("Contador de Repeticiones", "Cuenta automáticamente", Icons.Default.Add, Color(0xFF2196F3)),
                ToolItem("Calculadora RM", "1 Rep Max", Icons.Default.AccountBox, Color(0xFFFF9800)),
                ToolItem("Notas Rápidas", "Apuntes del entrenamiento", FitMeIcons.Weight, Color(0xFF9C27B0))
            )
        ) { tool ->
            WorkoutToolCard(tool)
        }
    }
}

@Composable
private fun WorkoutToolCard(tool: ToolItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {  },
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(tool.color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    tool.icon,
                    contentDescription = tool.title,
                    tint = tool.color,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tool.title,
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = tool.description,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                )
            }

            Icon(
                Icons.Default.MoreVert,
                contentDescription = "Abrir herramienta",
                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.4f),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// Data classes
private data class WorkoutItem(
    val id: String,
    val title: String,
    val duration: String,
    val exercises: String,
    val progress: Float,
    val isActive: Boolean
)

private data class HistoryItem(
    val date: String,
    val workoutName: String,
    val duration: String,
    val status: String,
    val completed: Boolean
)

private data class ToolItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color
)
