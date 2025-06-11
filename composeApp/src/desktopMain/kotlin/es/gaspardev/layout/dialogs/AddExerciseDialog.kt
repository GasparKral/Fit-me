package es.gaspardev.layout.dialogs

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import es.gaspardev.components.AutoCompleteTextField
import es.gaspardev.components.ToastManager
import es.gaspardev.core.domain.entities.workouts.Exercise
import es.gaspardev.core.domain.entities.workouts.WorkoutExercise
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.domain.usecases.read.workout.GetAvailableExercises
import es.gaspardev.enums.BodyPart
import es.gaspardev.enums.WeekDay
import es.gaspardev.helpers.resBodyPart
import es.gaspardev.helpers.resWeekDay
import es.gaspardev.icons.FitMeIcons

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddExerciseDialog(
    onCreateExercise: (WorkoutExercise, WeekDay) -> Unit = { _, _ -> },
    onCancel: () -> Unit = {},
) {
    var reps by remember { mutableStateOf("") }
    var sets by remember { mutableStateOf("") }
    var isOptional by remember { mutableStateOf(false) }
    var selectedExercise by remember { mutableStateOf<Exercise?>(null) }
    var selectedWeekDay by remember { mutableStateOf<WeekDay?>(null) }
    var exerciseSearchQuery by remember { mutableStateOf("") }
    var weekDayExpanded by remember { mutableStateOf(false) }
    var availableExercises: List<Exercise> by remember { mutableStateOf(emptyList()) }

    LaunchedEffect(Unit) {
        GetAvailableExercises().run(UseCase.None).fold(
            { list -> availableExercises = list },
            { err -> ToastManager.showError(err.message!!) }
        )
    }

    // Validación
    val isFormValid = selectedExercise != null &&
            selectedWeekDay != null &&
            reps.isNotEmpty() &&
            sets.isNotEmpty() &&
            reps.toIntOrNull() != null &&
            sets.toIntOrNull() != null &&
            reps.toIntOrNull()!! > 0 &&
            sets.toIntOrNull()!! > 0

    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Header mejorado
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = FitMeIcons.Weight,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = "Agregar Ejercicio al Workout",
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSurface
                )
            }

            Text(
                text = "Selecciona un ejercicio y configura las repeticiones y series",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 40.dp)
            )
        }

        Divider(
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
            thickness = 1.dp
        )

        // Selección de ejercicio mejorada
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = FitMeIcons.Weight,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Ejercicio *",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.onSurface
                )
            }

            AutoCompleteTextField(
                initialValue = selectedExercise?.name ?: "",
                onValueChange = { query ->
                    exerciseSearchQuery = query
                    if (selectedExercise != null && query != selectedExercise!!.name) {
                        selectedExercise = null
                    }
                },
                onItemSelected = { exercise ->
                    selectedExercise = exercise
                    exerciseSearchQuery = exercise.name
                },
                options = availableExercises,
                maxShowElements = 25,
                getFilterableText = { it.name },
                modifier = Modifier.fillMaxWidth(),
                placeHolder = {
                    Text(
                        "Buscar ejercicio...",
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                },
                dropDownComponent = { exercise ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RectangleShape,
                        backgroundColor = Color.Transparent,
                        elevation = 0.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = exercise.name,
                                style = MaterialTheme.typography.body1,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colors.onSurface
                            )
                            Text(
                                text = "${resBodyPart(exercise.bodyPart)} • ${exercise.description}",
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                                maxLines = 1
                            )
                        }
                    }
                },
                isError = exerciseSearchQuery.isNotEmpty() && selectedExercise == null,
                errorMessage = if (exerciseSearchQuery.isNotEmpty() && selectedExercise == null)
                    "Selecciona un ejercicio de la lista" else null
            )
        }

        // Mostrar información del ejercicio seleccionado mejorada
        selectedExercise?.let { exercise ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.05f),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colors.primary.copy(alpha = 0.2f)
                ),
                elevation = 0.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = exercise.name,
                            style = MaterialTheme.typography.subtitle1,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                    Text(
                        text = "Grupo muscular: ${resBodyPart(exercise.bodyPart)}",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = exercise.description,
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // Día de la semana mejorado
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = FitMeIcons.Calendar,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Día de la Semana *",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.onSurface
                )
            }

            ExposedDropdownMenuBox(
                expanded = weekDayExpanded,
                onExpandedChange = { weekDayExpanded = !weekDayExpanded }
            ) {
                OutlinedTextField(
                    value = selectedWeekDay?.let { resWeekDay(it) } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    placeholder = {
                        Text(
                            "Seleccionar día...",
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = weekDayExpanded)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = selectedWeekDay == null && (selectedExercise != null || reps.isNotEmpty() || sets.isNotEmpty()),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colors.primary,
                        unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                        errorBorderColor = MaterialTheme.colors.error,
                        backgroundColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                ExposedDropdownMenu(
                    expanded = weekDayExpanded,
                    onDismissRequest = { weekDayExpanded = false }
                ) {
                    val orderedWeekDays = listOf(
                        WeekDay.MONDAY, WeekDay.TUESDAY, WeekDay.WEDNESDAY,
                        WeekDay.THURSDAY, WeekDay.FRIDAY, WeekDay.SATURDAY, WeekDay.SUNDAY
                    )

                    orderedWeekDays.forEach { weekDay ->
                        DropdownMenuItem(
                            onClick = {
                                selectedWeekDay = weekDay
                                weekDayExpanded = false
                            }
                        ) {
                            Text(
                                resWeekDay(weekDay),
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                }
            }
        }

        // Fila mejorada con repeticiones y series
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Repeticiones mejoradas
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = FitMeIcons.ChartBar,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Repeticiones *",
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colors.onSurface
                    )
                }

                OutlinedTextField(
                    value = reps,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.matches(Regex("^\\d+$"))) {
                            reps = newValue
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "12",
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    isError = reps.isNotEmpty() && (reps.toIntOrNull() == null || reps.toIntOrNull()!! <= 0),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colors.primary,
                        unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                        errorBorderColor = MaterialTheme.colors.error,
                        backgroundColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                if (reps.isNotEmpty() && (reps.toIntOrNull() == null || reps.toIntOrNull()!! <= 0)) {
                    Text(
                        text = "Número válido requerido",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            // Series mejoradas
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = FitMeIcons.ChartBar,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Series *",
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colors.onSurface
                    )
                }

                OutlinedTextField(
                    value = sets,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.matches(Regex("^\\d+$"))) {
                            sets = newValue
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "3",
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    isError = sets.isNotEmpty() && (sets.toIntOrNull() == null || sets.toIntOrNull()!! <= 0),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colors.primary,
                        unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                        errorBorderColor = MaterialTheme.colors.error,
                        backgroundColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                if (sets.isNotEmpty() && (sets.toIntOrNull() == null || sets.toIntOrNull()!! <= 0)) {
                    Text(
                        text = "Número válido requerido",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }

        // Checkbox mejorado para ejercicio opcional
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = if (isOptional) MaterialTheme.colors.secondary.copy(alpha = 0.1f)
            else MaterialTheme.colors.onSurface.copy(alpha = 0.05f),
            shape = RoundedCornerShape(12.dp),
            elevation = 0.dp,
            border = if (isOptional) BorderStroke(1.dp, MaterialTheme.colors.secondary.copy(alpha = 0.3f))
            else BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isOptional = !isOptional }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Checkbox(
                    checked = isOptional,
                    onCheckedChange = { isOptional = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colors.secondary,
                        uncheckedColor = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Ejercicio opcional",
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colors.onSurface
                    )
                    Text(
                        text = "Los ejercicios opcionales pueden ser omitidos por el deportista si es necesario",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }

        Divider(
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
            thickness = 1.dp
        )

        // Botones mejorados
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End)
        ) {
            OutlinedButton(
                onClick = { onCancel() },
                modifier = Modifier.height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
                ),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
                )
            ) {
                Text(
                    "Cancelar",
                    style = MaterialTheme.typography.button,
                    fontWeight = FontWeight.Medium
                )
            }

            Button(
                onClick = {
                    if (isFormValid) {
                        val workoutExercise = WorkoutExercise(
                            reps = reps.toInt(),
                            sets = sets.toInt(),
                            isOption = isOptional,
                            exercise = selectedExercise!!
                        )
                        onCreateExercise(workoutExercise, selectedWeekDay!!)
                    }
                },
                enabled = isFormValid,
                modifier = Modifier.height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary,
                    disabledBackgroundColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
                )
            ) {
                Text(
                    "Agregar Ejercicio",
                    style = MaterialTheme.typography.button,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

