package es.gaspardev.core.domain.entities

import es.gaspardev.enums.WeekDay
import kotlinx.serialization.Serializable

@Serializable
data class Sportman(
    val user: User,
    var trainner: Trainner,
    var age: Int,
    var weight: Double,
    var height: Double,
    val sex: Boolean, // 1 hombre, 0 mujer
    val allergies: List<String>,
    val workouts: Workout?,
    val diet: Diet?,
    val suplementation: Map<WeekDay, List<Suplemment>>?
) {

}