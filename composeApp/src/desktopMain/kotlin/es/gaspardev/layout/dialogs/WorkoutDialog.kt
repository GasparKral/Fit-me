package es.gaspardev.layout.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.gaspardev.core.domain.entities.workouts.Workout
import es.gaspardev.core.domain.entities.workouts.WorkoutTemplate
import es.gaspardev.enums.Difficulty
import es.gaspardev.enums.OpeningMode
import es.gaspardev.enums.WorkoutType
import es.gaspardev.helpers.resDifficulty
import es.gaspardev.helpers.resWeekDay
import es.gaspardev.helpers.resWorkoutType
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.DialogState
import es.gaspardev.utils.toWeeks
import fit_me.composeapp.generated.resources.*
import fit_me.composeapp.generated.resources.Res
import fit_me.composeapp.generated.resources.cancel
import fit_me.composeapp.generated.resources.close
import fit_me.composeapp.generated.resources.weeks
import org.jetbrains.compose.resources.stringResource
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkoutDialog(
    workout: Workout = Workout(),
    template: WorkoutTemplate? = null,
    mode: OpeningMode = OpeningMode.CREATION,
    onAcceptAction: (Workout) -> Unit
) {
    val isNotEditable = mode == OpeningMode.VISUALIZE

    var state: Workout by remember {
        mutableStateOf(
            if (template == null) {
                workout
            } else {
                Workout(
                    null,
                    template.name,
                    template.description,
                    template.difficulty,
                    workoutType = template.workoutType,
                    exercises = template.exercises
                )
            }
        )
    }

    // Expanded states for dropdowns - disabled when not editable
    var typeExpanded by remember { mutableStateOf(false) }
    var difficultyExpanded by remember { mutableStateOf(false) }
    var durationExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(24.dp)
    ) {
        // Header - Dynamic title based on mode
        Text(
            text = when (mode) {
                OpeningMode.CREATION -> "Create New Workout Plan"
                OpeningMode.EDIT -> "Edit Workout Plan"
                OpeningMode.VISUALIZE -> "Workout Plan Details"
            },
            style = MaterialTheme.typography.h3,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = when (mode) {
                OpeningMode.CREATION -> "Create a new workout plan for your athletes. You can add exercises after creating the basic plan."
                OpeningMode.EDIT -> "Modify your workout plan details and exercises."
                OpeningMode.VISUALIZE -> "View the details of this workout plan."
            },
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Plan Name - Read-only when not editable
        OutlinedTextField(
            value = state.name,
            onValueChange = { if (!isNotEditable) state = state.copy(name = it) },
            label = { Text("Plan Name") },
            placeholder = { Text("e.g., 8-Week Strength Building") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isNotEditable,
            readOnly = isNotEditable
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Description - Read-only when not editable
        OutlinedTextField(
            value = state.description,
            onValueChange = { if (!isNotEditable) state = state.copy(description = it) },
            label = { Text("Description") },
            placeholder = { Text("Describe the goals and focus of this workout plan") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            maxLines = 4,
            enabled = !isNotEditable,
            readOnly = isNotEditable
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Type and Difficulty Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Type Dropdown - Disabled when not editable
            ExposedDropdownMenuBox(
                expanded = typeExpanded && !isNotEditable,
                onExpandedChange = { if (!isNotEditable) typeExpanded = !typeExpanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = resWorkoutType(state.workoutType),
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Type") },
                    placeholder = { Text("Select type") },
                    trailingIcon = {
                        if (!isNotEditable) {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isNotEditable
                )
                if (!isNotEditable) {
                    ExposedDropdownMenu(
                        expanded = typeExpanded,
                        onDismissRequest = { typeExpanded = false }
                    ) {
                        WorkoutType.entries.filter { it != WorkoutType.ALL }.forEach { type ->
                            DropdownMenuItem(
                                onClick = {
                                    state = state.copy(workoutType = type)
                                    typeExpanded = false
                                }
                            ) { Text(resWorkoutType(type)) }
                        }
                    }
                }
            }

            // Difficulty Dropdown - Disabled when not editable
            ExposedDropdownMenuBox(
                expanded = difficultyExpanded && !isNotEditable,
                onExpandedChange = { if (!isNotEditable) difficultyExpanded = !difficultyExpanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = resDifficulty(state.difficulty),
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Difficulty") },
                    placeholder = { Text("Select difficulty") },
                    trailingIcon = {
                        if (!isNotEditable) {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = difficultyExpanded)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isNotEditable
                )
                if (!isNotEditable) {
                    ExposedDropdownMenu(
                        expanded = difficultyExpanded,
                        onDismissRequest = { difficultyExpanded = false }
                    ) {
                        Difficulty.entries.forEach { difficulty ->
                            DropdownMenuItem(
                                onClick = {
                                    state = state.copy(difficulty = difficulty)
                                    difficultyExpanded = false
                                }
                            ) { Text(resDifficulty(difficulty)) }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Duration and Frequency Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Duration Dropdown - Disabled when not editable
            ExposedDropdownMenuBox(
                expanded = durationExpanded && !isNotEditable,
                onExpandedChange = { if (!isNotEditable) durationExpanded = !durationExpanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = state.duration.toWeeks().format(stringResource(Res.string.weeks)),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Duration") },
                    placeholder = { Text("Select duration") },
                    trailingIcon = {
                        if (!isNotEditable) {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = durationExpanded)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isNotEditable
                )
                if (!isNotEditable) {
                    ExposedDropdownMenu(
                        expanded = durationExpanded,
                        onDismissRequest = { durationExpanded = false }
                    ) {
                        listOf(
                            "2 ${stringResource(Res.string.weeks)}" to 2,
                            "4 ${stringResource(Res.string.weeks)}" to 4,
                            "6 ${stringResource(Res.string.weeks)}" to 6,
                            "8 ${stringResource(Res.string.weeks)}" to 8,
                            "12 ${stringResource(Res.string.weeks)}" to 12
                        ).forEach { duration ->
                            DropdownMenuItem(
                                onClick = {
                                    state = state.copy(duration = (duration.second * 7).toDuration(DurationUnit.DAYS))
                                    durationExpanded = false
                                }
                            ) { Text(duration.first) }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Exercises Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ejercicios del Plan",
                style = MaterialTheme.typography.h3,
                fontWeight = FontWeight.Medium
            )

            // Hide "Add Exercise" button when not editable
            if (!isNotEditable) {
                OutlinedButton(
                    onClick = {
                        DialogState.changeContent {
                            AddExerciseDialog(
                                onCreateExercise = { exercise, weekday ->
                                    state.exercises[weekday]?.add(exercise) ?: state.exercises.put(
                                        weekday,
                                        mutableListOf(exercise)
                                    )
                                },
                                onCancel = {
                                    DialogState.changeContent {
                                        WorkoutDialog(state, mode = mode, onAcceptAction = onAcceptAction)
                                    }
                                }
                            )
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Agregar Ejercicio")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Exercises List
        if (state.exercises.values.flatten().isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.height(400.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.exercises.entries.forEach { (weekDay, exercises) ->
                    if (exercises.isNotEmpty()) {
                        item {
                            Text(
                                text = resWeekDay(weekDay),
                                style = MaterialTheme.typography.h6,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }

                    itemsIndexed(exercises) { index, exercise ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = exercise.exercise.name,
                                        style = MaterialTheme.typography.body1,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "${exercise.sets} series × ${exercise.reps} repeticiones",
                                        style = MaterialTheme.typography.body2,
                                        color = MaterialTheme.colors.onSurface
                                    )
                                }

                                if (!isNotEditable) {
                                    IconButton(
                                        onClick = {
                                            state = state.copy(
                                                exercises = state.exercises.mapValues { (day, dayExercise) ->
                                                    if (day == weekDay) {
                                                        dayExercise.filterIndexed { i, _ -> i != index }.toMutableList()
                                                    } else {
                                                        dayExercise
                                                    }
                                                }.toMutableMap()
                                            )
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove exercise"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Empty state
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = FitMeIcons.Weight,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (isNotEditable) "No hay ejercicios en este plan" else "No hay ejercicios agregados",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface
                )
                if (!isNotEditable) {
                    Text(
                        text = "Haz clic en \"Agregar Ejercicio\" para comenzar",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End)
        ) {
            OutlinedButton(onClick = { DialogState.close() }) {
                Text(if (mode == OpeningMode.VISUALIZE) stringResource(Res.string.close) else stringResource(Res.string.cancel))
            }
            if (mode != OpeningMode.VISUALIZE) {
                Button(
                    onClick = {
                        onAcceptAction(state)
                        DialogState.close()
                    }
                ) {
                    Text(
                        if (mode == OpeningMode.CREATION) stringResource(Res.string.create_workout_plan) else stringResource(
                            Res.string.edit_workout
                        )
                    )
                }
            }
        }
    }
}