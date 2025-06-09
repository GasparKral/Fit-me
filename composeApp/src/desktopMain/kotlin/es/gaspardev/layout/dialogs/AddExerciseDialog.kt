package es.gaspardev.layout.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import es.gaspardev.components.AutoCompleteTextField
import es.gaspardev.core.domain.entities.workouts.Exercise
import es.gaspardev.core.domain.entities.workouts.WorkoutExecise
import es.gaspardev.enums.WeekDay
import es.gaspardev.helpers.resBodyPart
import es.gaspardev.helpers.resWeekDay

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddExerciseDialog(
    onCreateExercise: (WorkoutExecise, WeekDay) -> Unit = { _, _ -> },
    onCancel: () -> Unit = {},
    modifier: Modifier = Modifier,
    availableExercises: List<Exercise> = getSampleExercises(),
) {
    var reps by remember { mutableStateOf("") }
    var sets by remember { mutableStateOf("") }
    var isOptional by remember { mutableStateOf(false) }
    var selectedExercise by remember { mutableStateOf<Exercise?>(null) }
    var selectedWeekDay by remember { mutableStateOf<WeekDay?>(null) }
    var exerciseSearchQuery by remember { mutableStateOf("") }
    var weekDayExpanded by remember { mutableStateOf(false) }

    // Validación
    val isFormValid = selectedExercise != null &&
            selectedWeekDay != null &&
            reps.isNotEmpty() &&
            sets.isNotEmpty() &&
            reps.toIntOrNull() != null &&
            sets.toIntOrNull() != null &&
            reps.toIntOrNull()!! > 0 &&
            sets.toIntOrNull()!! > 0

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Text(
                text = "Agregar Ejercicio al Workout",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Selecciona un ejercicio y configura las repeticiones y series",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )

            Divider()

            // Selección de ejercicio con AutoCompleteTextField
            Column {
                Text(
                    text = "Ejercicio *",
                    style = MaterialTheme.typography.subtitle2,
                    fontWeight = FontWeight.Medium
                )

                AutoCompleteTextField(
                    initialValue = exerciseSearchQuery,
                    onValueChange = { query ->
                        exerciseSearchQuery = query
                        // Si el query no coincide con el ejercicio seleccionado, deseleccionarlo
                        if (selectedExercise != null && query != selectedExercise!!.name) {
                            selectedExercise = null
                        }
                    },
                    onItemSelected = { exercise ->
                        selectedExercise = exercise
                        exerciseSearchQuery = exercise.name
                    },
                    options = availableExercises,
                    getFilterableText = { it.name },
                    modifier = Modifier.fillMaxWidth(),
                    label = { },
                    placeHolder = { Text("Buscar ejercicio...") },
                    dropDownComponent = { exercise ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = exercise.name,
                                style = MaterialTheme.typography.body2,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${resBodyPart(exercise.bodyPart)} • ${exercise.description}",
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                                maxLines = 1
                            )
                        }
                    },
                    isError = exerciseSearchQuery.isNotEmpty() && selectedExercise == null,
                    errorMessage = if (exerciseSearchQuery.isNotEmpty() && selectedExercise == null)
                        "Selecciona un ejercicio de la lista" else null
                )
            }

            // Mostrar información del ejercicio seleccionado
            selectedExercise?.let { exercise ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = MaterialTheme.colors.surface,
                    elevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = exercise.name,
                            style = MaterialTheme.typography.subtitle1,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Grupo muscular: ${resBodyPart(exercise.bodyPart)}",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = exercise.description,
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            // Día de la semana
            Column {
                Text(
                    text = "Día de la Semana *",
                    style = MaterialTheme.typography.subtitle2,
                    fontWeight = FontWeight.Medium
                )

                ExposedDropdownMenuBox(
                    expanded = weekDayExpanded,
                    onExpandedChange = { weekDayExpanded = !weekDayExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedWeekDay?.let { resWeekDay(it) } ?: "",
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Seleccionar día...") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = weekDayExpanded) },
                        modifier = Modifier.fillMaxWidth(),
                        isError = selectedWeekDay == null && (selectedExercise != null || reps.isNotEmpty() || sets.isNotEmpty())
                    )

                    ExposedDropdownMenu(
                        expanded = weekDayExpanded,
                        onDismissRequest = { weekDayExpanded = false }
                    ) {
                        // Mostrar los días en orden de lunes a domingo
                        val orderedWeekDays = listOf(
                            WeekDay.MONDAY,
                            WeekDay.TUESDAY,
                            WeekDay.WEDNESDAY,
                            WeekDay.THURSDAY,
                            WeekDay.FRIDAY,
                            WeekDay.SATURDAY,
                            WeekDay.SUNDAY
                        )

                        orderedWeekDays.forEach { weekDay ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedWeekDay = weekDay
                                    weekDayExpanded = false
                                }
                            ) {
                                Text(resWeekDay(weekDay))
                            }
                        }
                    }
                }
            }

            // Fila con repeticiones y series
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Repeticiones
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Repeticiones *",
                        style = MaterialTheme.typography.subtitle2,
                        fontWeight = FontWeight.Medium
                    )

                    OutlinedTextField(
                        value = reps,
                        onValueChange = { newValue ->
                            // Solo permitir números
                            if (newValue.isEmpty() || newValue.matches(Regex("^\\d+$"))) {
                                reps = newValue
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("12") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        isError = reps.isNotEmpty() && (reps.toIntOrNull() == null || reps.toIntOrNull()!! <= 0)
                    )

                    if (reps.isNotEmpty() && (reps.toIntOrNull() == null || reps.toIntOrNull()!! <= 0)) {
                        Text(
                            text = "Ingresa un número válido",
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.error,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }
                }

                // Series
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Series *",
                        style = MaterialTheme.typography.subtitle2,
                        fontWeight = FontWeight.Medium
                    )

                    OutlinedTextField(
                        value = sets,
                        onValueChange = { newValue ->
                            // Solo permitir números
                            if (newValue.isEmpty() || newValue.matches(Regex("^\\d+$"))) {
                                sets = newValue
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("3") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        isError = sets.isNotEmpty() && (sets.toIntOrNull() == null || sets.toIntOrNull()!! <= 0)
                    )

                    if (sets.isNotEmpty() && (sets.toIntOrNull() == null || sets.toIntOrNull()!! <= 0)) {
                        Text(
                            text = "Ingresa un número válido",
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.error,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }
                }
            }

            // Checkbox para ejercicio opcional
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isOptional,
                    onCheckedChange = { isOptional = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colors.primary
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Ejercicio opcional",
                    style = MaterialTheme.typography.body2
                )
            }

            if (isOptional) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = MaterialTheme.colors.surface,
                    elevation = 1.dp
                ) {
                    Text(
                        text = "ℹ️ Los ejercicios opcionales pueden ser omitidos por el deportista si es necesario.",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Divider()

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End)
            ) {
                OutlinedButton(
                    onClick = { onCancel() },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colors.onSurface
                    )
                ) {
                    Text("Cancelar")
                }

                Button(
                    onClick = {
                        if (isFormValid) {
                            val workoutExercise = WorkoutExecise(
                                reps = reps.toInt(),
                                sets = sets.toInt(),
                                isOption = isOptional,
                                exercise = selectedExercise!!,
                                notes = emptyList() // Se pueden agregar notas después
                            )
                            onCreateExercise(workoutExercise, selectedWeekDay!!)
                        }
                    },
                    enabled = isFormValid,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary
                    )
                ) {
                    Text("Agregar Ejercicio")
                }
            }
        }
    }
}

// Función de ejemplo para simular datos de ejercicios
// En una implementación real, esto vendría de tu repositorio/API
private fun getSampleExercises(): List<Exercise> {
    return listOf(
        Exercise(1, "Press de banca", "Ejercicio básico para desarrollo del pecho", es.gaspardev.enums.BodyPart.CHEST),
        Exercise(2, "Sentadillas", "Ejercicio fundamental para piernas y glúteos", es.gaspardev.enums.BodyPart.LEG),
        Exercise(
            3,
            "Peso muerto",
            "Ejercicio compuesto que trabaja toda la cadena posterior",
            es.gaspardev.enums.BodyPart.BACK
        ),
        Exercise(4, "Press militar", "Ejercicio de empuje vertical para hombros", es.gaspardev.enums.BodyPart.SHOULDER),
        Exercise(5, "Curl de bíceps", "Ejercicio de aislamiento para bíceps", es.gaspardev.enums.BodyPart.ARM),
        Exercise(6, "Plancha", "Ejercicio isométrico para fortalecer el core", es.gaspardev.enums.BodyPart.CORE),
        Exercise(
            7,
            "Burpees",
            "Ejercicio de alta intensidad para todo el cuerpo",
            es.gaspardev.enums.BodyPart.FULL_BODY
        ),
        Exercise(
            8,
            "Flexiones",
            "Ejercicio de empuje para pecho, hombros y tríceps",
            es.gaspardev.enums.BodyPart.CHEST
        ),
        Exercise(9, "Dominadas", "Ejercicio de tracción para espalda y bíceps", es.gaspardev.enums.BodyPart.BACK),
        Exercise(10, "Zancadas", "Ejercicio unilateral para piernas y glúteos", es.gaspardev.enums.BodyPart.LEG),
        Exercise(
            11,
            "Press inclinado",
            "Variante de press para la parte superior del pecho",
            es.gaspardev.enums.BodyPart.CHEST
        ),
        Exercise(
            12,
            "Remo con barra",
            "Ejercicio de tracción horizontal para espalda",
            es.gaspardev.enums.BodyPart.BACK
        ),
        Exercise(
            13,
            "Elevaciones laterales",
            "Ejercicio de aislamiento para hombros",
            es.gaspardev.enums.BodyPart.SHOULDER
        ),
        Exercise(
            14,
            "Extensión de tríceps",
            "Ejercicio para la parte posterior del brazo",
            es.gaspardev.enums.BodyPart.ARM
        ),
        Exercise(15, "Abdominales", "Ejercicio básico para el recto abdominal", es.gaspardev.enums.BodyPart.CORE)
    )
}