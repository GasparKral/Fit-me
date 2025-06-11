package es.gaspardev.layout.workouts

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.gaspardev.components.AssistChip
import es.gaspardev.components.DifficultyBadge
import es.gaspardev.components.ToastManager
import es.gaspardev.core.domain.entities.workouts.Workout
import es.gaspardev.core.domain.entities.workouts.WorkoutTemplate
import es.gaspardev.core.domain.usecases.delete.workout.DeleteWorkoutTemplate
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.DialogState
import es.gaspardev.layout.dialogs.WorkoutDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WorkoutTemplateCard(
    template: WorkoutTemplate,
    scope: CoroutineScope,
    onDeleteAction: (Int) -> Unit,
    onAcceptAction: (Workout) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Column {
                Text(
                    text = template.name,
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = template.description,
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    maxLines = 2,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(Modifier.height(16.dp))
            DifficultyBadge(template.difficulty)
            // Badges row
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AssistChip(
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
            Row {
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            DeleteWorkoutTemplate().run(template.templateId!!).fold(
                                { _ -> onDeleteAction(template.templateId!!) },
                                { err -> ToastManager.showError(err.message!!) }
                            )
                        }
                    }
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null)
                    Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                    Text("Elimar Plantilla", color = MaterialTheme.colors.primary.copy(alpha = .6f))
                }
                Spacer(Modifier.width(6.dp))
                Button(
                    onClick = {
                        DialogState.openWith { WorkoutDialog(template = template, onAcceptAction = onAcceptAction) }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Usar Plantilla")
                }
            }
        }
    }
}