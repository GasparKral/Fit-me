package es.gaspardev.layout.athletes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.gaspardev.core.domain.entities.workouts.CompletionWorkoutStatistic
import es.gaspardev.enums.OpeningMode
import es.gaspardev.helpers.toSpainDate
import es.gaspardev.helpers.toSpainTime
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.DialogState
import es.gaspardev.layout.dialogs.WorkoutDialog


@Composable
fun WorkoutHistoryItem(history: CompletionWorkoutStatistic) {
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
                    text = history.workout.name,
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
                            text = history.completeAt.toSpainDate(),
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
                            text = history.workout.duration.toSpainTime(),
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colors.surface
                    ) {
                        Text(
                            text = history.workout.workoutType.name,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.subtitle2
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {

                IconButton(onClick = {
                    DialogState.openWith {
                        WorkoutDialog(workout = history.workout, mode = OpeningMode.VISUALIZE) {
                            DialogState.close()
                        }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "View details"
                    )
                }
            }
        }

    }
}