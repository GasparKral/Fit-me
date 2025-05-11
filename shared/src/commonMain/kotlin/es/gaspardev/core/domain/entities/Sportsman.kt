package es.gaspardev.core.domain.entities

import es.gaspardev.enums.WeekDay
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
class Sportsman(
    val user: User,
    var trainer: Trainer?,
    var age: Int,
    var weight: Double?,
    var height: Double?,
    val sex: Boolean, // 1 hombre, 0 mujer
    val allergies: List<String>?,
    val workouts: Workout?,
    val diet: Diet?,
    val suplementation: Map<WeekDay, List<Suplemment>>?,
    val status: SportsmanStatus
) {

    companion object {
        const val URLPATH = "/users/sportmant"
    }

    fun calculateIMC(): Double {
        return if (weight != null && height != null) weight!! / (height!! * height!!) else throw IllegalArgumentException(
            "Faltan datos para hacer la medición"
        )
    }

}

@Serializable
data class SportsmanStatus(
    val status: Boolean, // 1 = activo,0 = inactivo
    val lastActive: kotlinx.datetime.Instant,
    val needsAttetion: Boolean
) {
    fun getLastConectionTimeDiference(): Duration {
        return Clock.System.now().minus(lastActive)
    }
}
