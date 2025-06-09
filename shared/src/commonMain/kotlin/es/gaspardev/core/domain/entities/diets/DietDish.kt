package es.gaspardev.core.domain.entities.diets

import es.gaspardev.enums.MealType
import kotlinx.serialization.Serializable

@Serializable
data class DietDish(
    var amout: Double,
    var mealType: MealType,
    var dish: Dish
)
