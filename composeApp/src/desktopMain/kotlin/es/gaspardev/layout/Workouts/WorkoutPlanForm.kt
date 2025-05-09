package es.gaspardev.layout.Workouts

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkoutPlanForm(
    formData: WorkoutPlanFormData = WorkoutPlanFormData(),
    onFormChange: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Plan Name
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Plan Name",
                style = MaterialTheme.typography.subtitle1
            )
            OutlinedTextField(
                value = formData.name,
                onValueChange = { onFormChange("name", it) },
                placeholder = { Text("e.g., 8-Week Strength Building") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = MaterialTheme.shapes.small
            )
        }

        // Description
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Description",
                style = MaterialTheme.typography.subtitle1
            )
            OutlinedTextField(
                value = formData.description,
                onValueChange = { onFormChange("description", it) },
                placeholder = { Text("Describe the goals and focus of this workout plan") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                maxLines = 4,
                shape = MaterialTheme.shapes.small
            )
        }

        // Type and Difficulty Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Type Dropdown
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Type",
                    style = MaterialTheme.typography.subtitle1
                )
                var typeExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = typeExpanded,
                    onExpandedChange = { typeExpanded = !typeExpanded }
                ) {
                    OutlinedTextField(
                        value = formData.type.ifEmpty { "Select type" },
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.small
                    )
                    ExposedDropdownMenu(
                        expanded = typeExpanded,
                        onDismissRequest = { typeExpanded = false }
                    ) {
                        listOf(
                            "Strength", "Cardio", "Full Body",
                            "Upper Body", "Lower Body", "Core", "Flexibility"
                        ).forEach { type ->
                            DropdownMenuItem(
                                onClick = {
                                    onFormChange("type", type.lowercase())
                                    typeExpanded = false
                                }
                            ) { Text(type) }
                        }
                    }
                }
            }

            // Difficulty Dropdown
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Difficulty",
                    style = MaterialTheme.typography.subtitle1
                )
                var difficultyExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = difficultyExpanded,
                    onExpandedChange = { difficultyExpanded = !difficultyExpanded }
                ) {
                    OutlinedTextField(
                        value = formData.difficulty.ifEmpty { "Select difficulty" },
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = difficultyExpanded) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.small
                    )
                    ExposedDropdownMenu(
                        expanded = difficultyExpanded,
                        onDismissRequest = { difficultyExpanded = false }
                    ) {
                        listOf("Beginner", "Intermediate", "Advanced").forEach { difficulty ->
                            DropdownMenuItem(
                                onClick = {
                                    onFormChange("difficulty", difficulty.lowercase())
                                    difficultyExpanded = false
                                }
                            ) { Text(difficulty) }
                        }
                    }
                }
            }
        }

        // Duration and Frequency Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Duration Dropdown
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Duration",
                    style = MaterialTheme.typography.subtitle1
                )
                var durationExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = durationExpanded,
                    onExpandedChange = { durationExpanded = !durationExpanded }
                ) {
                    OutlinedTextField(
                        value = formData.duration.ifEmpty { "Select duration" },
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = durationExpanded) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.small
                    )
                    ExposedDropdownMenu(
                        expanded = durationExpanded,
                        onDismissRequest = { durationExpanded = false }
                    ) {
                        listOf(
                            "2 weeks", "4 weeks", "6 weeks",
                            "8 weeks", "12 weeks"
                        ).forEach { duration ->
                            DropdownMenuItem(
                                onClick = {
                                    onFormChange("duration", duration)
                                    durationExpanded = false
                                }
                            ) { Text(duration) }
                        }
                    }
                }
            }

            // Frequency Dropdown
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Frequency",
                    style = MaterialTheme.typography.subtitle1
                )
                var frequencyExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = frequencyExpanded,
                    onExpandedChange = { frequencyExpanded = !frequencyExpanded }
                ) {
                    OutlinedTextField(
                        value = formData.frequency.ifEmpty { "Select frequency" },
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = frequencyExpanded) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.small
                    )
                    ExposedDropdownMenu(
                        expanded = frequencyExpanded,
                        onDismissRequest = { frequencyExpanded = false }
                    ) {
                        listOf(
                            "2 days/week", "3 days/week", "4 days/week",
                            "5 days/week", "6 days/week"
                        ).forEach { frequency ->
                            DropdownMenuItem(
                                onClick = {
                                    onFormChange("frequency", frequency)
                                    frequencyExpanded = false
                                }
                            ) { Text(frequency) }
                        }
                    }
                }
            }
        }
    }
}

// Data class to hold form data
data class WorkoutPlanFormData(
    val name: String = "",
    val description: String = "",
    val type: String = "",
    val difficulty: String = "",
    val duration: String = "",
    val frequency: String = ""
)