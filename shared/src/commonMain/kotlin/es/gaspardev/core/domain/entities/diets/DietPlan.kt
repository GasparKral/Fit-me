package es.gaspardev.core.domain.entities.diets

import es.gaspardev.enums.DietType
import es.gaspardev.enums.WeekDay
import kotlinx.serialization.Serializable

@Serializable
data class DietPlan(
    val dietId: Int,
    var name: String,
    var description: String,
    var type: DietType,
    var duration: String,
    val frequency: String,
    val asignedCount: Int,
    var dishes: Map<WeekDay, List<DietDish>>
)