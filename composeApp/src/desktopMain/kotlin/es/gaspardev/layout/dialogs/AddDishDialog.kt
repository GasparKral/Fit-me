package es.gaspardev.layout.dialogs

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import es.gaspardev.core.domain.entities.diets.DietDish
import es.gaspardev.core.domain.entities.diets.Dish
import es.gaspardev.enums.MealType
import es.gaspardev.enums.WeekDay
import es.gaspardev.helpers.resMealType
import es.gaspardev.helpers.resWeekDay
import es.gaspardev.components.AutoCompleteTextField
import es.gaspardev.icons.FitMeIcons

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddDishDialog(
    onCreateDish: (DietDish, WeekDay) -> Unit = { _, _ -> },
    onCancel: () -> Unit = {},
    modifier: Modifier = Modifier,
    availableDishes: List<Dish> = getSampleDishes(),
) {
    var amount by remember { mutableStateOf("") }
    var selectedMealType by remember { mutableStateOf<MealType?>(null) }
    var selectedDish by remember { mutableStateOf<Dish?>(null) }
    var selectedWeekDay by remember { mutableStateOf<WeekDay?>(null) }
    var dishSearchQuery by remember { mutableStateOf("") }
    var mealTypeExpanded by remember { mutableStateOf(false) }
    var weekDayExpanded by remember { mutableStateOf(false) }

    // Validación
    val isFormValid = selectedDish != null &&
            selectedMealType != null &&
            selectedWeekDay != null &&
            amount.isNotEmpty() &&
            amount.toDoubleOrNull() != null &&
            amount.toDoubleOrNull()!! > 0


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
                    imageVector = FitMeIcons.Nutrition,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = "Agregar Plato a la Dieta",
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSurface
                )
            }

            Text(
                text = "Completa la información del plato para agregarlo al plan de dieta",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 40.dp)
            )
        }

        Divider(
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
            thickness = 1.dp
        )

        // Selección de plato mejorada
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = FitMeIcons.Nutrition,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Plato *",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.onSurface
                )
            }

            AutoCompleteTextField(
                initialValue = selectedDish?.name ?: "",
                onValueChange = { query ->
                    dishSearchQuery = query
                    if (selectedDish != null && query != selectedDish!!.name) {
                        selectedDish = null
                    }
                },
                onItemSelected = { dish ->
                    selectedDish = dish
                    dishSearchQuery = dish.name
                },
                options = availableDishes,
                getFilterableText = { it.name },
                modifier = Modifier.fillMaxWidth(),
                placeHolder = {
                    Text(
                        "Buscar plato...",
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                },
                dropDownComponent = { dish ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(8.dp),
                        backgroundColor = Color.Transparent,
                        elevation = 0.dp
                    ) {
                        Text(
                            text = dish.name,
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onSurface,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                },
                isError = dishSearchQuery.isNotEmpty() && selectedDish == null,
                errorMessage = if (dishSearchQuery.isNotEmpty() && selectedDish == null)
                    "Selecciona un plato de la lista" else null
            )
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
                    isError = selectedWeekDay == null && (selectedDish != null || amount.isNotEmpty() || selectedMealType != null),
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

        // Fila mejorada con cantidad y tipo de comida
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cantidad mejorada
            Column(
                modifier = Modifier.weight(1f),
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
                        text = "Cantidad (g) *",
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colors.onSurface
                    )
                }

                OutlinedTextField(
                    value = amount,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                            amount = newValue
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "100",
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    isError = amount.isNotEmpty() && (amount.toDoubleOrNull() == null || amount.toDoubleOrNull()!! <= 0),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colors.primary,
                        unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                        errorBorderColor = MaterialTheme.colors.error,
                        backgroundColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                if (amount.isNotEmpty() && (amount.toDoubleOrNull() == null || amount.toDoubleOrNull()!! <= 0)) {
                    Text(
                        text = "Cantidad válida requerida",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            // Tipo de comida mejorado
            Column(
                modifier = Modifier.weight(1f),
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
                        text = "Comida *",
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colors.onSurface
                    )
                }

                ExposedDropdownMenuBox(
                    expanded = mealTypeExpanded,
                    onExpandedChange = { mealTypeExpanded = !mealTypeExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedMealType?.let { resMealType(it) } ?: "",
                        onValueChange = {},
                        readOnly = true,
                        placeholder = {
                            Text(
                                "Seleccionar...",
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = mealTypeExpanded)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        isError = selectedMealType == null && amount.isNotEmpty(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colors.primary,
                            unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                            errorBorderColor = MaterialTheme.colors.error,
                            backgroundColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = mealTypeExpanded,
                        onDismissRequest = { mealTypeExpanded = false }
                    ) {
                        MealType.entries.forEach { mealType ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedMealType = mealType
                                    mealTypeExpanded = false
                                }
                            ) {
                                Text(
                                    resMealType(mealType),
                                    style = MaterialTheme.typography.body1
                                )
                            }
                        }
                    }
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
                        val dietDish = DietDish(
                            amout = amount.toDouble(),
                            mealType = selectedMealType!!,
                            dish = selectedDish!!
                        )
                        onCreateDish(dietDish, selectedWeekDay!!)
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
                    "Agregar Plato",
                    style = MaterialTheme.typography.button,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// Función de ejemplo para simular datos de la DB
private fun getSampleDishes(): List<Dish> {
    return listOf(
        Dish(1, "Pechuga de pollo a la plancha"),
        Dish(2, "Arroz integral"),
        Dish(3, "Ensalada mixta"),
        Dish(4, "Avena con frutas"),
        Dish(5, "Salmón al horno"),
        Dish(6, "Batata asada"),
        Dish(7, "Yogur griego con nueces"),
        Dish(8, "Atún con verduras"),
        Dish(9, "Quinoa con verduras"),
        Dish(10, "Batido de proteínas"),
        Dish(11, "Tortilla de claras"),
        Dish(12, "Tostada de aguacate"),
        Dish(13, "Verduras a la parrilla"),
        Dish(14, "Pasta integral"),
        Dish(15, "Lentejas estofadas"),
        Dish(16, "Merluza al vapor"),
        Dish(17, "Brócoli al vapor"),
        Dish(18, "Almendras"),
        Dish(19, "Manzana"),
        Dish(20, "Plátano")
    )
}