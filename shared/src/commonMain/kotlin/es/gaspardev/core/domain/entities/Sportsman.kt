package es.gaspardev.core.domain.entities

import es.gaspardev.auxliars.Either
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
class Sportsman(
    val user: User,
    var trainer: Trainer? = null,
    var age: Int = 14,
    val sex: Boolean = true, // 1 hombre, 0 mujer
    val trainingSince: Instant? = null,
    val allergies: List<String> = listOf("TEST"),
    var workouts: Workout? = null,
    var diet: Diet? = null,
    var mesasurements: Measurements = Measurements()
) {

    companion object {
        const val URLPATH = "/users/sportmant"
    }

    fun calculateIMC(): Either<IllegalArgumentException, Double> {
        return if (mesasurements.weight != null && mesasurements.height != null) Either.Success(mesasurements.weight!! / (mesasurements.height!! * mesasurements.height!!))
        else throw IllegalArgumentException("Faltan datos para hacer la medición")
    }

    fun getWorkoutProgression(): Int {
        return if (workouts == null) 0
        else {
            minOf(
                ((Clock.System.now()
                    .toEpochMilliseconds() / (workouts!!.initalDate + workouts!!.duration!!).toEpochMilliseconds()) * 100).toInt(),
                100
            )
        }
    }

    fun getDietProgression(): Int {
        return if (diet == null) 0
        else {
            minOf(
                ((Clock.System.now()
                    .toEpochMilliseconds() / (diet!!.initialDate + diet!!.duration).toEpochMilliseconds()) * 100).toInt(),
                100
            )
        }
    }
}


