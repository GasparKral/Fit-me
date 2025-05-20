package es.gaspardev.core.domain.entities

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

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
) {

    companion object {
        const val URLPATH = "/users/sportmant"
    }

    fun calculateIMC(): Double {
        return if (weight != null && height != null) weight!! / (height!! * height!!) else throw IllegalArgumentException(
            "Faltan datos para hacer la medición"
        )
    }

    fun getWorkoutProgression(): Int {
        return if (workouts == null) 0
        else {
            minOf(
                ((Clock.System.now()
                    .toEpochMilliseconds() / (workouts.initalDate + workouts.duration!!).toEpochMilliseconds()) * 100).toInt(),
                100
            )
        }
    }

    fun getDietProgression(): Int {
        return if (diet == null) 0
        else {
            minOf(
                ((Clock.System.now()
                    .toEpochMilliseconds() / (diet.initialDate + diet.duration).toEpochMilliseconds()) * 100).toInt(),
                100
            )
        }
    }
}


