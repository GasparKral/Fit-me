package es.gaspardev.layout.workouts

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import es.gaspardev.components.AssistChip
import es.gaspardev.components.DifficultyBadge
import es.gaspardev.components.DropdownMenuButton
import es.gaspardev.components.ToastManager
import es.gaspardev.core.domain.entities.workouts.Workout
import es.gaspardev.core.domain.entities.workouts.WorkoutPlan
import es.gaspardev.core.domain.usecases.create.CreateNewWorkout
import es.gaspardev.core.domain.usecases.delete.DeleteWorkout
import es.gaspardev.core.domain.usecases.update.UpdateWorkout
import es.gaspardev.enums.OpeningMode
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.DialogState
import es.gaspardev.layout.dialogs.AsignDialog
import es.gaspardev.layout.dialogs.WorkoutDialog
import es.gaspardev.states.LoggedTrainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlin.time.Duration

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WorkoutPlanCard(
    plan: WorkoutPlan,
    scope: CoroutineScope,
    onDuplicationAction: (WorkoutPlan) -> Unit = {},
    onAsign: (WorkoutPlan) -> Unit = {},
    onEditAction: (WorkoutPlan) -> Unit = {},
    onDeleteAction: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = Color.White,
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
                        style = MaterialTheme.typography.h3,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = plan.description,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                DropdownMenuButton(
                    items = listOf(
                        { Text("Edit Plan", style = MaterialTheme.typography.subtitle2) },
                        { Text("Duplicate Plan", style = MaterialTheme.typography.subtitle2) },
                        { Text("Assign to Athlete", style = MaterialTheme.typography.subtitle2) },
                        { Text("Delete Plan", style = MaterialTheme.typography.subtitle2) }
                    ),
                    onItemSelected = { index, funtion ->
                        when (index) {
                            0 -> {
                                DialogState.openWith {
                                    WorkoutDialog(
                                        Workout(
                                            id = plan.workoutId,
                                            name = plan.name,
                                            description = plan.description,
                                            difficulty = plan.difficulty,
                                            duration = Duration.parse(plan.duration),
                                            startAt = Instant.DISTANT_FUTURE,
                                            workoutType = plan.type,
                                            exercises = plan.exercises,
                                            notes = listOf()
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
                                funtion(false)
                            }

                            1 -> {
                                val workout = Workout(
                                    id = null,
                                    name = plan.name + " copy",
                                    description = plan.description,
                                    difficulty = plan.difficulty,
                                    duration = Duration.parse(plan.duration),
                                    startAt = Instant.DISTANT_FUTURE,
                                    workoutType = plan.type,
                                    exercises = plan.exercises,
                                    notes = listOf()
                                )

                                scope.launch {
                                    CreateNewWorkout().run(Pair(workout, LoggedTrainer.state.trainer!!)).fold(
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
                                funtion(false)
                            }

                            2 -> {
                                DialogState.openWith {
                                    AsignDialog(
                                        {
                                            WorkoutPlanCard(
                                                plan,
                                                scope
                                            )
                                        }
                                    ) { onAsign(plan.copy(asignedCount = plan.asignedCount + 1)) }
                                }
                                funtion(false)
                            }

                            3 -> {
                                scope.launch {
                                    DeleteWorkout().run(plan.workoutId).fold(
                                        { onDeleteAction() },
                                        { err -> ToastManager.showError(err.message!!) }
                                    )
                                }
                                funtion(false)
                            }
                        }
                    },
                )
            }

            Spacer(Modifier.height(16.dp))
            DifficultyBadge(plan.difficulty.toString())
            // Badges row
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AssistChip(
                    onClick = {},
                    label = { Text(plan.type.toString()) },
                    leadingIcon = {
                        Icon(
                            FitMeIcons.Weight,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
                AssistChip(
                    onClick = {},
                    label = { Text(plan.duration) },
                    leadingIcon = {
                        Icon(
                            FitMeIcons.Calendar,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )

                AssistChip(
                    onClick = {},
                    label = { Text(plan.frequency) },
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
                    DialogState.openWith {
                        WorkoutDialog(
                            Workout(
                                id = plan.workoutId,
                                name = plan.name,
                                description = plan.description,
                                difficulty = plan.difficulty,
                                duration = Duration.parse(plan.duration),
                                startAt = Instant.DISTANT_FUTURE,
                                workoutType = plan.type,
                                exercises = plan.exercises,
                                notes = listOf()
                            ),
                            mode = OpeningMode.VISUALIZE
                        ) {
                            scope.launch {
                                UpdateWorkout().run(plan).fold(
                                    { value -> onEditAction(value) },
                                    { err -> ToastManager.showError(err.message!!) }
                                )
                            }
                        }
                    }
                }) {
                    Text("View Details")
                }
            }
        }
    }
}