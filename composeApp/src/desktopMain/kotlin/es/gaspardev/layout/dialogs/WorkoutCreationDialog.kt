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
import es.gaspardev.enums.WorkoutType
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.DialogState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkoutCreationDialog(
    template: WorkoutTemplate? = null,
    onCreatePlan: (Workout) -> Unit
) {
    val state: Workout by remember {
        mutableStateOf(
            if (template == null) {
                Workout()
            } else {
                Workout(
                    template.name,
                    template.description,
                    template.difficulty,
                    workoutType = template.workoutType,
                    exercises = template.exercises
                )
            }
        )
    }
    // var assignedTo: Athlete? by remember { mutableStateOf(null) }

    // Expanded states for dropdowns
    var typeExpanded by remember { mutableStateOf(false) }
    var difficultyExpanded by remember { mutableStateOf(false) }
    var durationExpanded by remember { mutableStateOf(false) }
    // var frequencyExpanded by remember { mutableStateOf(false) }
    var showExerciseSelector by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(24.dp)
    ) {
        // Header
        Text(
            text = "Create New Workout Plan",
            style = MaterialTheme.typography.h3,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Create a new workout plan for your athletes. You can add exercises after creating the basic plan.",
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Plan Name
        OutlinedTextField(
            value = state.name,
            onValueChange = { state.name = it },
            label = { Text("Plan Name") },
            placeholder = { Text("e.g., 8-Week Strength Building") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Description
        OutlinedTextField(
            value = state.description,
            onValueChange = { state.description = it },
            label = { Text("Description") },
            placeholder = { Text("Describe the goals and focus of this workout plan") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            maxLines = 4
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Type and Difficulty Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Type Dropdown
            ExposedDropdownMenuBox(
                expanded = typeExpanded,
                onExpandedChange = { typeExpanded = !typeExpanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = state.workoutType.toString(),
                    onValueChange = { state.workoutType = WorkoutType.valueOf(it) },
                    readOnly = true,
                    label = { Text("Type") },
                    placeholder = { Text("Select type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = typeExpanded,
                    onDismissRequest = { typeExpanded = false }
                ) {
                    WorkoutType.entries.forEach { type ->
                        DropdownMenuItem(
                            onClick = {
                                typeExpanded = false
                            }
                        ) { Text(type.toString()) }
                    }
                }
            }

            // Difficulty Dropdown
            ExposedDropdownMenuBox(
                expanded = difficultyExpanded,
                onExpandedChange = { difficultyExpanded = !difficultyExpanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = state.difficulty.toString(),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Difficulty") },
                    placeholder = { Text("Select difficulty") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = difficultyExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = difficultyExpanded,
                    onDismissRequest = { difficultyExpanded = false }
                ) {
                    Difficulty.entries.forEach { difficulty ->
                        DropdownMenuItem(
                            onClick = {
                                difficultyExpanded = false
                            }
                        ) { Text(difficulty.toString()) }
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
            // Duration Dropdown
            ExposedDropdownMenuBox(
                expanded = durationExpanded,
                onExpandedChange = { durationExpanded = !durationExpanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = state.difficulty.toString(),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Duration") },
                    placeholder = { Text("Select duration") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = durationExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                /*ExposedDropdownMenu(
                    expanded = durationExpanded,
                    onDismissRequest = { durationExpanded = false }
                ) {
                    listOf("2 weeks", "4 weeks", "6 weeks", "8 weeks", "12 weeks").forEach { duration ->
                        DropdownMenuItem(
                            text = { Text(duration) },
                            onClick = {
                                formData = formData.copy(duration = duration)
                                durationExpanded = false
                            }
                        )
                    }
                }*/
            }

            // Frequency Dropdown
            /*ExposedDropdownMenuBox(
                expanded = frequencyExpanded,
                onExpandedChange = { frequencyExpanded = !frequencyExpanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = formData.frequency,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Frequency") },
                    placeholder = { Text("Select frequency") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = frequencyExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = frequencyExpanded,
                    onDismissRequest = { frequencyExpanded = false }
                ) {
                    listOf(
                        "2 days/week",
                        "3 days/week",
                        "4 days/week",
                        "5 days/week",
                        "6 days/week"
                    ).forEach { frequency ->
                        DropdownMenuItem(
                            text = { Text(frequency) },
                            onClick = {
                                formData = formData.copy(frequency = frequency)
                                frequencyExpanded = false
                            }
                        )
                    }
                }
            }*/
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

            OutlinedButton(
                onClick = { showExerciseSelector = true }
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

        Spacer(modifier = Modifier.height(16.dp))

        // Exercises List
        if (state.exercises.entries.flatMap { it.value }.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.height(200.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(state.exercises.entries.flatMap { it.value }) { index, exercise ->
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

                            IconButton(
                                onClick = {

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
                    text = "No hay ejercicios agregados",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface
                )
                Text(
                    text = "Haz clic en \"Agregar Ejercicio\" para comenzar",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End)
        ) {
            OutlinedButton(onClick = { DialogState.close() }) {
                Text("Cancel")
            }

            Button(
                onClick = {
                    onCreatePlan(state)
                    DialogState.close()
                }
            ) {
                Text("Create Plan")
            }
        }
    }
}
