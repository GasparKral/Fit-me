package es.gaspardev.core.domain.entities.diets

import es.gaspardev.enums.DietType
import es.gaspardev.enums.WeekDay
import kotlinx.serialization.Serializable

@Serializable
data class DietTemplate(
    private val templateId: Int,
    var name: String,
    var description: String,
    var dietType: DietType,
    val dishes: MutableMap<WeekDay, MutableList<DietDish>>
) {

    companion object {
        fun fromDiet(diet: Diet): DietTemplate {
            return DietTemplate(
                templateId = diet.getId(),
                name = diet.name,
                description = diet.description,
                dietType = diet.dietType,
                dishes = diet.dishes
            )
        }
    }

    fun getId(): Int = templateId

}