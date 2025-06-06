package es.gaspardev.layout.workouts

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import es.gaspardev.components.AssistChip
import es.gaspardev.components.DifficultyBadge
import es.gaspardev.core.domain.entities.workouts.Workout
import es.gaspardev.core.domain.entities.workouts.WorkoutTemplate
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.DialogState
import es.gaspardev.layout.dialogs.WorkoutDialog


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WorkoutTemplateCard(template: WorkoutTemplate, onAcceptAction: (Workout) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Column {
                Text(
                    text = template.name,
                    style = MaterialTheme.typography.h3,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = template.description,
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.height(16.dp))
            DifficultyBadge(template.difficulty.toString())
            // Badges row
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AssistChip(
                    onClick = {},
                    label = { Text(template.workoutType.toString()) },
                    leadingIcon = {
                        Icon(
                            FitMeIcons.Weight,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )

            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "${template.exercises.values.size} exercises",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface
            )

            Spacer(Modifier.height(16.dp))

            Divider()

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    DialogState.openWith { WorkoutDialog(template = template, onAcceptAction = onAcceptAction) }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Use Template")
            }
        }
    }
}