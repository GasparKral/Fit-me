package es.gaspardev.core.domain.entities.diets

import kotlinx.serialization.Serializable

@Serializable
data class Dish(
    val id: Int,
    val name: String
)
