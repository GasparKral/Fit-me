package es.gaspardev.layout.athletes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.gaspardev.core.domain.entities.diets.CompletionDietStatistics
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.entities.workouts.CompletionWorkoutStatistic
import es.gaspardev.core.domain.usecases.read.user.athlete.GetAthleteDietHystory
import es.gaspardev.core.domain.usecases.read.user.athlete.GetAthleteWorkoutHistory
import es.gaspardev.icons.FitMeIcons
import fit_me.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun OverviewTab(
    athlete: Athlete
) {

    var workoutHistory: List<CompletionWorkoutStatistic> by remember { mutableStateOf(listOf()) }
    var dietsHistory: List<CompletionDietStatistics> by remember { mutableStateOf(listOf()) }

    LaunchedEffect(Unit) {
        GetAthleteWorkoutHistory().run(athlete).fold(
            { value -> workoutHistory = value }
        )
        GetAthleteDietHystory().run(athlete).fold(
            { value -> dietsHistory = value }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Stats section

        Text(
            text = stringResource(Res.string.stats),
            style = MaterialTheme.typography.h3,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(
                icon = Icons.Default.AccountBox,
                value = workoutHistory.size.toString(),
                label = stringResource(Res.string.workouts_completed),
                modifier = Modifier.weight(1f)
            )

            StatCard(
                icon = FitMeIcons.Weight,
                value = "${workoutHistory.map { it.workout.getWorkoutProgression() }.average().toInt()}%",
                label = stringResource(Res.string.avg_performance),
                modifier = Modifier.weight(1f)
            )

            StatCard(
                icon = FitMeIcons.Nutrition,
                value = "${dietsHistory.map { it.diet.getDietProgression() }.average().toInt()}%",
                label = stringResource(Res.string.diet_adherence),
                modifier = Modifier.weight(1f)
            )
        }


        // Recent Activity section
        Column(Modifier.fillMaxHeight(.5f)) {
            Text(
                text = stringResource(Res.string.recent_activity),
                style = MaterialTheme.typography.h3,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                items(workoutHistory) { historic ->
                    WorkoutItem(historic)
                }
            }
        }

    }
}