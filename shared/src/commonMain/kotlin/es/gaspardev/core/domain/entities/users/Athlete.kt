package es.gaspardev.core.domain.entities.users

import es.gaspardev.core.domain.entities.diets.Diet
import es.gaspardev.core.domain.entities.users.info.Allergy
import es.gaspardev.core.domain.entities.users.info.Measurements
import es.gaspardev.core.domain.entities.workouts.Workout
import es.gaspardev.enums.Sex
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
class Athlete(
    val user: User,
    var age: Int,
    var sex: Sex,
    val trainingSince: Instant,
    val allergies: List<Allergy>,
    val measurements: Measurements = Measurements(),
    var workout: Workout?,
    var diet: Diet?,
    var needAssistant: Boolean = false
) {

    companion object {
        const val URLPATH = "/users/athlete"
    }

    fun getOverallProgression(): Double {
        val wProgression = workout?.getWorkoutProgression() ?: 0.0
        val dProgression = diet?.getDietProgression() ?: 0.0

        // If both exist, calculate average
        return if (workout != null && diet != null) {
            (wProgression + dProgression) / 2.0
        }
        // If only workout exists, use its progression
        else if (workout != null) {
            wProgression
        }
        // If only diet exists, use its progression
        else if (diet != null) {
            dProgression
        }
        // If neither exists
        else {
            0.0
        }
    }

}