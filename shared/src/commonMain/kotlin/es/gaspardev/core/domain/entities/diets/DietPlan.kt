package es.gaspardev.core.domain.entities.diets

import es.gaspardev.enums.DietType
import es.gaspardev.enums.WeekDay
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class DietPlan(
    val dietId: Int,
    var name: String,
    var description: String,
    var type: DietType,
    var duration: Duration,
    val frequency: String,
    val asignedCount: Int,
    var dishes: MutableMap<WeekDay, MutableList<DietDish>>
)