package es.gaspardev.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import es.gaspardev.components.*
import es.gaspardev.core.LocalRouter
import es.gaspardev.core.domain.entities.Measurements
import es.gaspardev.core.domain.entities.Sportsman
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.athletes.MeasurementItem
import es.gaspardev.layout.athletes.OverviewTab
import es.gaspardev.layout.athletes.WorkoutsTab
import fit_me.composeapp.generated.resources.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource

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
        weight = 68.5,
        height = 170.toDouble(),
        bodyFat = 18,
        lastUpdated = Clock.System.now()
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
    sportsman: Sportsman
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Overview", "Workouts", "Nutrition", "Schedule")
    val router = LocalRouter.current
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
                onClick = { router.navigateTo(Routes.Athletes) },
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Volver")
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
                        UserAvatar(sportsman.user, LayoutDirection.Vertical)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Status badge
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (sportsman.user.status.status) Color(0xFF4CAF50) else Color.Gray,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = if (sportsman.user.status.status) stringResource(Res.string.active) else stringResource(
                                    Res.string.inactive
                                ),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                color = Color.White,
                                style = MaterialTheme.typography.subtitle1
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Member since and last active
                        Text(
                            text = stringResource(Res.string.member_since) + " ${
                                sportsman.trainingSince!!.toLocalDateTime(TimeZone.currentSystemDefault())
                                    .formatDateTime()
                            }",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface
                        )

                        LastActiveText(sportsman.user.status.lastActive)

                        if (sportsman.allergies.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(24.dp))

                            LazyColumn(
                                contentPadding = PaddingValues(6.dp),
                                modifier = Modifier.fillMaxWidth().heightIn(max = 150.dp)
                            ) {
                                items(sportsman.allergies.sorted()) { allergy ->
                                    AssistChip({}, label = { Text(allergy, style = MaterialTheme.typography.caption) })
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Progress
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(Res.string.overall_progress),
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
                                text = stringResource(Res.string.contact_info),
                                style = MaterialTheme.typography.body1,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            if (sportsman.user.phone != null) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = MaterialTheme.colors.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = sportsman.user.phone!!,
                                        style = MaterialTheme.typography.body2
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colors.onSurface
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = sportsman.user.email,
                                    style = MaterialTheme.typography.body2
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))

                            // Current Plans
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = stringResource(Res.string.current_plans),
                                    style = MaterialTheme.typography.body1,
                                    fontWeight = FontWeight.Medium
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = FitMeIcons.Weight,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = if (sportsman.workouts == null) MaterialTheme.colors.onSurface.copy(.6f) else MaterialTheme.colors.primary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = sportsman.workouts?.name
                                            ?: stringResource(Res.string.no_workout_plan_assigned),
                                        style = MaterialTheme.typography.body2
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = FitMeIcons.Nutrition,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = if (sportsman.diet == null) MaterialTheme.colors.onSurface.copy(.6f) else MaterialTheme.colors.primary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = sportsman.diet?.name ?: stringResource(Res.string.no_diet_plan_assigned),
                                        style = MaterialTheme.typography.body2
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Measurements
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = stringResource(Res.string.measurements),
                                    style = MaterialTheme.typography.body1,
                                    fontWeight = FontWeight.Medium
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    MeasurementItem(
                                        label = stringResource(Res.string.height),
                                        value = "${exampleAthlete.measurements.weight} Kg"
                                    )
                                    MeasurementItem(
                                        label = stringResource(Res.string.weight),
                                        value = " ${exampleAthlete.measurements.height} cm"
                                    )
                                    MeasurementItem(
                                        label = stringResource(Res.string.body_fat),
                                        value = "${exampleAthlete.measurements.bodyFat}%"
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = stringResource(Res.string.last_update) + ": ${
                                        exampleAthlete.measurements.lastUpdated?.toLocalDateTime(
                                            TimeZone.currentSystemDefault()
                                        )?.formatDateTime()
                                            ?: "Nunca"
                                    }",
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
                                    Text(stringResource(Res.string.send_message))
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
                                    Text(stringResource(Res.string.schedule))
                                }
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
fun NutritionTab(nutritionLogs: List<NutritionLog>) {
    // Implementación similar a WorkoutsTab pero con datos de nutrición
    // Por brevedad, no incluyo la implementación completa
}

@Composable
fun ScheduleTab(upcomingSessions: List<Session>) {
    // Implementación similar a WorkoutsTab pero con datos de sesiones programadas
    // Por brevedad, no incluyo la implementación completa
}

