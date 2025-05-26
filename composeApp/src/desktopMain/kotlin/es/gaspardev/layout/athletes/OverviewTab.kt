package es.gaspardev.layout.athletes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.pages.Athlete2
import es.gaspardev.pages.NutritionLog
import es.gaspardev.pages.Session
import es.gaspardev.pages.Workout2

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