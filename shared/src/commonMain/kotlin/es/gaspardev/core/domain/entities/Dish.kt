package es.gaspardev.core.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class Dish(
    var name: String,
    var ammount: Double,
    val optionalDishes: List<Dish>?
) {

}