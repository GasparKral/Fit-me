package es.gaspardev.core.domain.entities

import es.gaspardev.enums.WeekDay
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class Diet(
    val name: String,
    val description: String?,
    val notes: List<Note>,
    val meals: Map<WeekDay, List<Pair<Dish, Double>>>, // Dia de la semana: Lista<Plato, Cantidad>
    val duration: Duration,
    val initialDate: Instant
) {

}