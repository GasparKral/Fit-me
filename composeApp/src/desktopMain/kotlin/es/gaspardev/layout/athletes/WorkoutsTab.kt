package es.gaspardev.layout.athletes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.gaspardev.core.domain.entities.workouts.Workout
import es.gaspardev.icons.FitMeIcons
import fit_me.composeapp.generated.resources.Res
import fit_me.composeapp.generated.resources.assign_workout
import fit_me.composeapp.generated.resources.workout_history
import org.jetbrains.compose.resources.stringResource


@Composable
fun WorkoutsTab(workoutHistory: List<Workout>) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.workout_history),
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
                Text(stringResource(Res.string.assign_workout))
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
