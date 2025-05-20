package es.gaspardev.pages

// Iconos equivalentes en Compose
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import es.gaspardev.icons.FitMeIcons
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

data class Athlete2(
    val id: String,
    val name: String,
    val initials: String,
    val image: String?,
    val status: String,
    val joinDate: String,
    val lastActive: String,
    val progress: Int,
    val email: String,
    val phone: String,
    val workoutPlan: String?,
    val nutritionPlan: String?,
    val measurements: Measurements,
    val goals: List<String>,
    val completedWorkouts: Int,
    val upcomingSessions: Int
)

data class Measurements(
    val weight: String,
    val height: String,
    val bodyFat: String,
    val lastUpdated: String
)

data class Workout2(
    val id: String,
    val date: String,
    val name: String,
    val type: String,
    val duration: String,
    val completed: Boolean,
    val performance: Int
)

data class NutritionLog(
    val id: String,
    val date: String,
    val calories: Int,
    val protein: String,
    val carbs: String,
    val fat: String,
    val adherence: Int,
    val notes: String
)

data class Session(
    val id: String,
    val date: String,
    val time: String,
    val type: String,
    val name: String,
    val duration: String
)

// Ejemplo de Athlete2 con todas las propiedades incluidas
val exampleAthlete = Athlete2(
    id = "ATH123",
    name = "María Rodríguez",
    initials = "MR",
    image = "https://example.com/images/maria.jpg",
    status = "Active",
    joinDate = "2023-01-15",
    lastActive = "2023-11-20",
    progress = 75,
    email = "maria.rodriguez@example.com",
    phone = "+1234567890",
    workoutPlan = "Plan de fuerza avanzado",
    nutritionPlan = "Dieta alta en proteínas",
    measurements = Measurements(
        weight = "68.5 kg",
        height = "170 cm",
        bodyFat = "18%",
        lastUpdated = "2023-11-15"
    ),
    goals = listOf(
        "Perder 5% de grasa corporal",
        "Aumentar press banca en 10kg",
        "Correr 10km en menos de 45min"
    ),
    completedWorkouts = 42,
    upcomingSessions = 3
)

// Ejemplo de Workout
val exampleWorkout = listOf(
    Workout2(
        id = "WK789",
        date = "2023-11-20",
        name = "Entrenamiento de fuerza superior",
        type = "Fuerza",
        duration = "45 min",
        completed = true,
        performance = 85
    )
)

// Ejemplo de NutritionLog
val exampleNutritionLog = listOf(
    NutritionLog(
        id = "NL456",
        date = "2023-11-20",
        calories = 2350,
        protein = "180g",
        carbs = "220g",
        fat = "80g",
        adherence = 90,
        notes = "Cumplió con todas las comidas programadas"
    )
)

// Ejemplo de Session
val exampleSession = listOf(
    Session(
        id = "SES321",
        date = "2023-11-22",
        time = "18:00",
        type = "Personalizado",
        name = "Sesión de evaluación de progreso",
        duration = "60 min"
    )
)

@Composable
fun AthleteInfoScreen(

) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Overview", "Workouts", "Nutrition", "Schedule")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header with back and edit buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = {},
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Back to Athletes")
            }

            Button(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit Profile")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Main content
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // Profile card (left column)
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(end = 8.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colors.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            if (exampleAthlete.image != null) {
                                KamelImage(
                                    resource = asyncPainterResource(exampleAthlete.image),
                                    contentDescription = exampleAthlete.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Text(
                                    text = exampleAthlete.name,
                                    style = MaterialTheme.typography.h6
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Name and status
                        Text(
                            text = exampleAthlete.name,
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Status badge
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (exampleAthlete.status == "active") Color(0xFF4CAF50) else Color.Gray,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = exampleAthlete.status,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                color = Color.White,
                                style = MaterialTheme.typography.subtitle1
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Member since and last active
                        Text(
                            text = "Member since ${exampleAthlete.joinDate}",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface
                        )

                        Text(
                            text = "Last active: ${exampleAthlete.lastActive}",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Progress
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Overall Progress",
                                style = MaterialTheme.typography.body2
                            )
                            Text(
                                text = "${exampleAthlete.progress}%",
                                style = MaterialTheme.typography.body2
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        LinearProgressIndicator(
                            progress = exampleAthlete.progress / 100f,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Contact Information
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Contact Information",
                                style = MaterialTheme.typography.body1,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colors.onSurface
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = exampleAthlete.email,
                                    style = MaterialTheme.typography.body2
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = FitMeIcons.Messages,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colors.onSurface
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = exampleAthlete.phone,
                                    style = MaterialTheme.typography.body2
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Current Plans
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Current Plans",
                                style = MaterialTheme.typography.body1,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = FitMeIcons.Weight,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colors.primary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = exampleAthlete.workoutPlan ?: "No workout plan assigned",
                                    style = MaterialTheme.typography.body2
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = FitMeIcons.Nutrition,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colors.primary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = exampleAthlete.nutritionPlan ?: "No nutrition plan assigned",
                                    style = MaterialTheme.typography.body2
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Measurements
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Measurements",
                                style = MaterialTheme.typography.body1,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                MeasurementItem(label = "Weight", value = exampleAthlete.measurements.weight)
                                MeasurementItem(label = "Height", value = exampleAthlete.measurements.height)
                                MeasurementItem(label = "Body Fat", value = exampleAthlete.measurements.bodyFat)
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Last updated: ${exampleAthlete.measurements.lastUpdated}",
                                style = MaterialTheme.typography.subtitle2,
                                color = MaterialTheme.colors.onSurface,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Action buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { /* Message action */ },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = FitMeIcons.Messages,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Message")
                            }

                            OutlinedButton(
                                onClick = { /* Schedule action */ },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = FitMeIcons.Calendar,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Schedule")
                            }
                        }
                    }
                }
            }

            // Details card (right column)
            Card(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .padding(start = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Card header
                    Text(
                        text = "Athlete Details",
                        style = MaterialTheme.typography.h6
                    )

                    Text(
                        text = "View and manage athlete information",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tabs
                    TabRow(
                        selectedTabIndex = selectedTab,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                text = { Text(title) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tab content
                    when (selectedTab) {
                        0 -> OverviewTab(
                            athlete = exampleAthlete,
                            workoutHistory = exampleWorkout,
                            nutritionLogs = exampleNutritionLog,
                            upcomingSessions = exampleSession
                        )

                        1 -> WorkoutsTab(workoutHistory = exampleWorkout)
                        2 -> NutritionTab(nutritionLogs = exampleNutritionLog)
                        3 -> ScheduleTab(upcomingSessions = exampleSession)
                    }
                }
            }
        }
    }
}

@Composable
fun MeasurementItem(label: String, value: String) {
    Surface(
        modifier = Modifier.padding(4.dp),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colors.surface
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.onSurface
            )
            Text(
                text = value,
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OverviewTab(
    athlete: Athlete2,
    workoutHistory: List<Workout2>,
    nutritionLogs: List<NutritionLog>,
    upcomingSessions: List<Session>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Goals section
        item {
            Text(
                text = "Goals",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                athlete.goals.forEach { goal ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colors.surface
                    ) {
                        Text(
                            text = goal,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
            }
        }

        // Stats section
        item {
            Text(
                text = "Stats",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    icon = Icons.Default.AccountBox,
                    value = athlete.completedWorkouts.toString(),
                    label = "Workouts Completed",
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    icon = FitMeIcons.Calendar,
                    value = athlete.upcomingSessions.toString(),
                    label = "Upcoming Sessions",
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    icon = FitMeIcons.Weight,
                    value = "${workoutHistory.map { it.performance }.average().toInt()}%",
                    label = "Avg. Performance",
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    icon = FitMeIcons.Nutrition,
                    value = "${nutritionLogs.map { it.adherence }.average().toInt()}%",
                    label = "Diet Adherence",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Recent Activity section
        item {
            Text(
                text = "Recent Activity",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        items(workoutHistory) { workout ->
            WorkoutItem(workout = workout)
        }

        // Upcoming Sessions section
        item {
            Text(
                text = "Upcoming Sessions",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        items(upcomingSessions) { session ->
            SessionItem(session = session)
        }
    }
}

@Composable
fun StatCard(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = label,
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun WorkoutItem(workout: Workout2) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = workout.name,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = workout.date,
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface
                    )

                    Text(
                        text = "•",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface
                    )

                    Text(
                        text = workout.duration,
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colors.primary
            ) {
                Text(
                    text = "${workout.performance}% Performance",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }
    }
}

@Composable
fun SessionItem(session: Session) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = session.name,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = session.date,
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface
                    )

                    Text(
                        text = "•",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface
                    )

                    Text(
                        text = session.time,
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface
                    )

                    Text(
                        text = "•",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface
                    )

                    Text(
                        text = session.duration,
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colors.surface
            ) {
                Text(
                    text = session.type.replaceFirstChar { it.uppercase() },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }
    }
}

@Composable
fun WorkoutsTab(workoutHistory: List<Workout2>) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Workout History",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Medium
            )

            Button(onClick = { /* Assign workout action */ }) {
                Icon(
                    imageVector = FitMeIcons.Weight,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Assign Workout")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            items(workoutHistory) { workout ->
                WorkoutHistoryItem(workout = workout)
            }
        }
    }
}

@Composable
fun WorkoutHistoryItem(workout: Workout2) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colors.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = workout.name,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = FitMeIcons.Calendar,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = MaterialTheme.colors.onSurface
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = workout.date,
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = FitMeIcons.Calendar,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = MaterialTheme.colors.onSurface
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = workout.duration,
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colors.surface
                    ) {
                        Text(
                            text = workout.type.replaceFirstChar { it.uppercase() },
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.subtitle2
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Performance",
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Medium
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LinearProgressIndicator(
                            progress = workout.performance / 100f,
                            modifier = Modifier.width(100.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "${workout.performance}%",
                            style = MaterialTheme.typography.body2,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                IconButton(onClick = { /* View details action */ }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "View details"
                    )
                }
            }
        }

        Divider()
    }
}

@Composable
fun NutritionTab(nutritionLogs: List<NutritionLog>) {
    // Implementación similar a WorkoutsTab pero con datos de nutrición
    // Por brevedad, no incluyo la implementación completa
}

@Composable
fun ScheduleTab(upcomingSessions: List<Session>) {
    // Implementación similar a WorkoutsTab pero con datos de sesiones programadas
    // Por brevedad, no incluyo la implementación completa
}

