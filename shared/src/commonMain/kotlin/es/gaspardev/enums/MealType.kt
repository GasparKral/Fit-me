package es.gaspardev.enums

import kotlinx.serialization.Serializable

@Serializable
enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK,
    PRE_WORKOUT,
    POST_WORKOUT
}
