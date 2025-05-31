package es.gaspardev.layout.athletes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.gaspardev.core.domain.entities.workouts.Workout
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun WorkoutItem(workout: Workout) {
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
                        text = workout.startAt.toLocalDateTime(TimeZone.currentSystemDefault()).date.toString(),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface
                    )

                    Text(
                        text = "•",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface
                    )

                    Text(
                        text = workout.duration.toIsoString(),
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
                    text = "${workout.getWorkoutProgression()}% Performance",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }
    }
}