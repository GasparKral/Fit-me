package es.gaspardev.core.domain.entities.users

import es.gaspardev.core.domain.entities.diets.Diet
import es.gaspardev.core.domain.entities.users.info.Allergy
import es.gaspardev.core.domain.entities.users.info.Measurements
import es.gaspardev.core.domain.entities.workouts.Workout
import es.gaspardev.enums.Sex
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.math.RoundingMode

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

        val progression = if (workout != null && diet != null) {
            (wProgression + dProgression) / 2.0
        } else if (workout != null) {
            wProgression
        } else if (diet != null) {
            dProgression
        } else {
            0.0
        }

        return BigDecimal(progression).setScale(2, RoundingMode.HALF_UP).toDouble()
    }

}