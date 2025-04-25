package es.gaspardev.core.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class Dish(
    val id: Int,
    var name: String,
    val optionalDishes: List<Dish>?
) {

}