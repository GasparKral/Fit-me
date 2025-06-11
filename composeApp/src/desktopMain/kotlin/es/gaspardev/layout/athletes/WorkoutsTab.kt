package es.gaspardev.layout.athletes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.entities.workouts.CompletionWorkoutStatistic
import es.gaspardev.core.domain.usecases.read.user.athlete.GetAthleteWorkoutHistory
import fit_me.composeapp.generated.resources.Res
import fit_me.composeapp.generated.resources.workout_history
import org.jetbrains.compose.resources.stringResource


@Composable
fun WorkoutsTab(athlete: Athlete) {

    var workoutHistory: List<CompletionWorkoutStatistic> by remember { mutableStateOf(emptyList()) }

    LaunchedEffect(athlete) {
        GetAthleteWorkoutHistory().run(athlete).fold(
            { value -> workoutHistory = value }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(Res.string.workout_history),
            style = MaterialTheme.typography.h3,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            items(workoutHistory) { workout ->
                WorkoutHistoryItem(history = workout)
            }
        }
    }
}
