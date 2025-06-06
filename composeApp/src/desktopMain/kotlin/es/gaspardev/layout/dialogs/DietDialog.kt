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
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.DialogState
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DietDialog(
    diet: Diet = Diet(),
    template: DietTemplate? = null,
    onAcceptAction: (Diet) -> Unit
) {
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
                    dishes = template.dishes,
                    notes = emptyList()
                )
            }
        )
    }

    // Expanded states for dropdowns
    var typeExpanded by remember { mutableStateOf(false) }
    var durationExpanded by remember { mutableStateOf(false) }
    var showDishSelector by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(24.dp)
    ) {
        // Header
        Text(
            text = "Create New Diet Plan",
            style = MaterialTheme.typography.h3,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Create a new diet plan for your athletes. You can add dishes and meals after creating the basic plan.",
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Diet Name
        OutlinedTextField(
            value = state.name,
            onValueChange = { state = state.copy(name = it) },
            label = { Text("Diet Plan Name") },
            placeholder = { Text("e.g., 4-Week Muscle Gain Diet") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Description
        OutlinedTextField(
            value = state.description,
            onValueChange = { state = state.copy(description = it) },
            label = { Text("Description") },
            placeholder = { Text("Describe the goals and focus of this diet plan") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            maxLines = 4
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Type and Duration Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Diet Type Dropdown
            ExposedDropdownMenuBox(
                expanded = typeExpanded,
                onExpandedChange = { typeExpanded = !typeExpanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = state.dietType.toString(),
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Diet Type") },
                    placeholder = { Text("Select type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                    modifier = Modifier.fillMaxWidth()
                )
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
                            Text(type.toString())
                        }
                    }
                }
            }

            // Duration Dropdown
            ExposedDropdownMenuBox(
                expanded = durationExpanded,
                onExpandedChange = { durationExpanded = !durationExpanded },
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
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = durationExpanded) },
                    modifier = Modifier.fillMaxWidth()
                )
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

            OutlinedButton(
                onClick = { showDishSelector = true }
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

        Spacer(modifier = Modifier.height(16.dp))

        // Dishes List
        if (state.dishes.values.flatten().isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.height(200.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.dishes.entries.forEach { (weekDay, dishes) ->
                    if (dishes.isNotEmpty()) {
                        item {
                            Text(
                                text = weekDay.toString(),
                                style = MaterialTheme.typography.h6,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }

                        itemsIndexed(dishes) { _, dish ->
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
                                            text = "${dish.amout}g - ${dish.mealType}",
                                            style = MaterialTheme.typography.body2,
                                            color = MaterialTheme.colors.onSurface
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            // Remove dish logic would go here
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
                    text = "No hay platos agregados",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface
                )
                Text(
                    text = "Haz clic en \"Agregar Plato\" para comenzar",
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
                    onAcceptAction(state)
                    DialogState.close()
                }
            ) {
                Text("Create Diet Plan")
            }
        }
    }
}