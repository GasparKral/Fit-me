package es.gaspardev.core.domain.entities

import es.gaspardev.enums.WeekDay
import kotlinx.serialization.Serializable

@Serializable
data class Diet(
    val notes: List<Note>,
    val dishes: Map<WeekDay, List<Pair<Dish, Double>>> // Dia de la semana: Lista<Plato, Cantidad>
) {

}