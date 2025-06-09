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
import es.gaspardev.core.domain.entities.diets.Diet
import es.gaspardev.core.domain.entities.diets.DietTemplate
import es.gaspardev.enums.DietType
import es.gaspardev.enums.OpeningMode
import es.gaspardev.helpers.resDietType
import es.gaspardev.helpers.resMealType
import es.gaspardev.helpers.resWeekDay
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.DialogState
import fit_me.composeapp.generated.resources.Res
import fit_me.composeapp.generated.resources.cancel
import fit_me.composeapp.generated.resources.close
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.days

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DietDialog(
    diet: Diet = Diet(),
    template: DietTemplate? = null,
    mode: OpeningMode = OpeningMode.CREATION,
    onAcceptAction: (Diet) -> Unit
) {
    val isNotEditable = mode == OpeningMode.VISUALIZE

    var state: Diet by remember {
        mutableStateOf(
            if (template == null) {
                diet
            } else {
                Diet(
                    name = template.name,
                    description = template.description,
                    dietType = template.dietType,
                    duration = 28.days,
                    startAt = Clock.System.now(),
                    dishes = template.dishes
                )
            }
        )
    }

    // Expanded states for dropdowns - disabled when not editable
    var typeExpanded by remember { mutableStateOf(false) }
    var durationExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(24.dp)
    ) {
        // Header - Dynamic title based on mode
        Text(
            text = when (mode) {
                OpeningMode.CREATION -> "Create New Diet Plan"
                OpeningMode.EDIT -> "Edit Diet Plan"
                OpeningMode.VISUALIZE -> "Diet Plan Details"
            },
            style = MaterialTheme.typography.h3,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = when (mode) {
                OpeningMode.CREATION -> "Create a new diet plan for your athletes. You can add dishes and meals after creating the basic plan."
                OpeningMode.EDIT -> "Modify your diet plan details and dishes."
                OpeningMode.VISUALIZE -> "View the details of this diet plan."
            },
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Diet Name - Read-only when not editable
        OutlinedTextField(
            value = state.name,
            onValueChange = { if (!isNotEditable) state = state.copy(name = it) },
            label = { Text("Diet Plan Name") },
            placeholder = { Text("e.g., 4-Week Muscle Gain Diet") },
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
            placeholder = { Text("Describe the goals and focus of this diet plan") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            maxLines = 4,
            enabled = !isNotEditable,
            readOnly = isNotEditable
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Type and Duration Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Diet Type Dropdown - Disabled when not editable
            ExposedDropdownMenuBox(
                expanded = typeExpanded && !isNotEditable,
                onExpandedChange = { if (!isNotEditable) typeExpanded = !typeExpanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = resDietType(state.dietType),
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Diet Type") },
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
                        DietType.entries.filter { it != DietType.ALL }.forEach { type ->
                            DropdownMenuItem(
                                onClick = {
                                    state = state.copy(dietType = type)
                                    typeExpanded = false
                                }
                            ) {
                                Text(resDietType(type))
                            }
                        }
                    }
                }
            }

            // Duration Dropdown - Disabled when not editable
            ExposedDropdownMenuBox(
                expanded = durationExpanded && !isNotEditable,
                onExpandedChange = { if (!isNotEditable) durationExpanded = !durationExpanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = when (state.duration.inWholeDays) {
                        14L -> "2 weeks"
                        28L -> "4 weeks"
                        42L -> "6 weeks"
                        56L -> "8 weeks"
                        84L -> "12 weeks"
                        else -> "${state.duration.inWholeDays} days"
                    },
                    onValueChange = { },
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
                            "2 weeks" to 14.days,
                            "4 weeks" to 28.days,
                            "6 weeks" to 42.days,
                            "8 weeks" to 56.days,
                            "12 weeks" to 84.days
                        ).forEach { (label, duration) ->
                            DropdownMenuItem(
                                onClick = {
                                    state = state.copy(duration = duration)
                                    durationExpanded = false
                                }
                            ) {
                                Text(label)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Dishes Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Platos del Plan",
                style = MaterialTheme.typography.h3,
                fontWeight = FontWeight.Medium
            )

            if (!isNotEditable) {
                OutlinedButton(
                    onClick = {
                        DialogState.changeContent {
                            AddDishDialog(
                                onCreateDish = { dish, weekday ->
                                    state.dishes[weekday]?.add(dish) ?: state.dishes.put(
                                        weekday,
                                        mutableListOf(dish)
                                    )
                                },
                                onCancel = {
                                    DialogState.changeContent {
                                        DietDialog(state, mode = mode, onAcceptAction = onAcceptAction)
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
                    Text("Agregar Plato")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dishes List
        if (state.dishes.values.flatten().isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.height(400.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.dishes.entries.forEach { (weekDay, dishes) ->
                    if (dishes.isNotEmpty()) {
                        item {
                            Text(
                                text = resWeekDay(weekDay),
                                style = MaterialTheme.typography.h6,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }

                        itemsIndexed(dishes.sortedBy { it.mealType }) { index, dish ->
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
                                            text = dish.dish.name,
                                            style = MaterialTheme.typography.body1,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = "${dish.amout}g - ${resMealType(dish.mealType)}",
                                            style = MaterialTheme.typography.body2,
                                            color = MaterialTheme.colors.onSurface
                                        )
                                    }

                                    if (!isNotEditable) {
                                        IconButton(
                                            onClick = {
                                                state = state.copy(
                                                    dishes = state.dishes.mapValues { (day, dayDishes) ->
                                                        if (day == weekDay) {
                                                            dayDishes.filterIndexed { i, _ -> i != index }
                                                                .toMutableList()
                                                        } else {
                                                            dayDishes
                                                        }
                                                    }.toMutableMap()
                                                )
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Remove dish"
                                            )
                                        }
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
                    imageVector = FitMeIcons.Weight, // You might want to use a food/diet icon instead
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (isNotEditable) "No hay platos en este plan" else "No hay platos agregados",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface
                )
                if (!isNotEditable) {
                    Text(
                        text = "Haz clic en \"Agregar Plato\" para comenzar",
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
                        when (mode) {
                            OpeningMode.CREATION -> "Create Diet Plan"
                            OpeningMode.EDIT -> "Save Changes"
                            else -> "Create Diet Plan"
                        }
                    )
                }
            }
        }
    }
}