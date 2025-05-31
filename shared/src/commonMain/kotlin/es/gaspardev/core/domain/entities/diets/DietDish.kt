package es.gaspardev.core.domain.entities.diets

import es.gaspardev.core.domain.entities.Note
import es.gaspardev.enums.MealType
import kotlinx.serialization.Serializable

@Serializable
data class DietDish(
    var amout: Double,
    var mealType: MealType,
    val notes: List<Note>,
    var dish: Dish
)
