package es.gaspardev.core.domain.entities.diets

import es.gaspardev.enums.DietType
import es.gaspardev.enums.WeekDay
import kotlinx.serialization.Serializable

@Serializable
data class DietTemplate(
    var templateId: Int? = null,
    var name: String,
    var description: String,
    var dietType: DietType,
    val dishes: MutableMap<WeekDay, MutableList<DietDish>>
) {


    companion object {
        fun fromDiet(diet: Diet): DietTemplate {
            val template = DietTemplate(
                templateId = diet.getId(),
                name = diet.name,
                description = diet.description,
                dietType = diet.dietType,
                dishes = diet.dishes
            )
            return template
        }
    }


}