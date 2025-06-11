package es.gaspardev.layout.workouts

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.gaspardev.components.AssistChip
import es.gaspardev.components.DifficultyBadge
import es.gaspardev.components.DropdownMenuButton
import es.gaspardev.components.ToastManager
import es.gaspardev.core.domain.entities.workouts.Workout
import es.gaspardev.core.domain.entities.workouts.WorkoutPlan
import es.gaspardev.core.domain.usecases.create.workout.CreateNewWorkout
import es.gaspardev.core.domain.usecases.delete.workout.DeleteWorkout
import es.gaspardev.core.domain.usecases.update.workout.AssignWorkoutToAthlete
import es.gaspardev.core.domain.usecases.update.workout.UpdateWorkout
import es.gaspardev.enums.OpeningMode
import es.gaspardev.helpers.resDifficulty
import es.gaspardev.helpers.resWorkoutType
import es.gaspardev.helpers.visualizeWorkout
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.DialogState
import es.gaspardev.layout.dialogs.AsignDialog
import es.gaspardev.layout.dialogs.WorkoutDialog
import es.gaspardev.states.LoggedTrainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WorkoutPlanCard(
    plan: WorkoutPlan,
    scope: CoroutineScope,
    onDuplicationAction: (WorkoutPlan) -> Unit = {},
    onAssignAction: (WorkoutPlan) -> Unit = {},
    onEditAction: (WorkoutPlan) -> Unit = {},
    onDeleteAction: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = plan.name,
                        style = MaterialTheme.typography.h4,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = plan.description,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                        maxLines = 2,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                DropdownMenuButton(
                    items = listOf(
                        { Text("Edit Plan", style = MaterialTheme.typography.subtitle2) },
                        { Text("Duplicate Plan", style = MaterialTheme.typography.subtitle2) },
                        { Text("Assign to Athlete", style = MaterialTheme.typography.subtitle2) },
                        { Text("Delete Plan", style = MaterialTheme.typography.subtitle2) }
                    ),
                    onItemSelected = { index, continueOpen ->
                        when (index) {
                            0 -> {
                                DialogState.openWith {
                                    WorkoutDialog(
                                        Workout(
                                            _id = plan.workoutId,
                                            name = plan.name,
                                            description = plan.description,
                                            difficulty = plan.difficulty,
                                            duration = plan.duration,
                                            startAt = Instant.DISTANT_FUTURE,
                                            workoutType = plan.type,
                                            exercises = plan.exercises
                                        ),
                                        mode = OpeningMode.EDIT
                                    ) {
                                        scope.launch {
                                            UpdateWorkout().run(plan).fold(
                                                { value -> onEditAction(value) },
                                                { err -> ToastManager.showError(err.message!!) }
                                            )
                                        }
                                    }
                                }
                                continueOpen(false)
                            }

                            1 -> {
                                val workout = Workout(
                                    _id = null,
                                    name = plan.name + " copy",
                                    description = plan.description,
                                    difficulty = plan.difficulty,
                                    duration = plan.duration,
                                    startAt = Instant.DISTANT_FUTURE,
                                    workoutType = plan.type,
                                    exercises = plan.exercises
                                )

                                scope.launch {
                                    CreateNewWorkout().run(Pair(workout, LoggedTrainer.state.trainer)).fold(
                                        { value ->
                                            onDuplicationAction(
                                                plan.copy(
                                                    workoutId = value,
                                                    name = plan.name + " copy"
                                                )
                                            )
                                        },
                                        { err -> ToastManager.showError(err.message!!) }
                                    )
                                }
                                continueOpen(false)
                            }

                            2 -> {
                                DialogState.openWith {
                                    AsignDialog(
                                        plan = {
                                            Card {
                                                Column {
                                                    Text(plan.name)
                                                    Text(plan.description)
                                                }

                                            }

                                        },
                                        onAcceptAction = { athlete ->
                                            scope.launch {
                                                AssignWorkoutToAthlete().run(Pair(plan, athlete)).fold(
                                                    { value ->
                                                        onAssignAction(value)
                                                        ToastManager.showSuccess("Se ha asignado correctamente")
                                                    },
                                                    { err -> ToastManager.showError(err.message!!) }
                                                )
                                            }
                                        },
                                        onCancel = {}
                                    )
                                }
                                continueOpen(false)
                            }

                            3 -> {
                                scope.launch {
                                    DeleteWorkout().run(plan.workoutId!!).fold(
                                        { onDeleteAction() },
                                        { err -> ToastManager.showError(err.message!!) }
                                    )
                                }
                                continueOpen(false)
                            }
                        }
                    },
                )
            }

            Spacer(Modifier.height(16.dp))
            DifficultyBadge(plan.difficulty)
            // Badges row
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AssistChip(
                    label = { Text(resWorkoutType(plan.type)) },
                    leadingIcon = {
                        Icon(
                            FitMeIcons.Weight,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
                AssistChip(
                    label = { Text(plan.duration.toString()) },
                    leadingIcon = {
                        Icon(
                            FitMeIcons.Calendar,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )

                AssistChip(
                    label = { Text("Días de entrenamiento: " + plan.frequency) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )


            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${plan.exercises.values.sumOf { it.size }} exercises",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface
                )
            }

            Spacer(Modifier.height(16.dp))

            Divider()

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colors.onSurface
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Assigned to ${plan.asignedCount} athletes",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface
                    )
                }

                TextButton(onClick = {
                    visualizeWorkout(Workout.fromPlan(plan))
                }) {
                    Text("View Details")
                }
            }
        }
    }
}