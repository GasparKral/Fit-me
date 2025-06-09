package es.gaspardev.database.entities

import es.gaspardev.core.domain.entities.diets.Diet
import es.gaspardev.core.domain.entities.diets.DietDish
import es.gaspardev.core.domain.entities.diets.DietPlan
import es.gaspardev.core.domain.entities.diets.DietTemplate
import es.gaspardev.database.*
import es.gaspardev.enums.WeekDay
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID


class DishEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DishEntity>(Dishes)

    var name by Dishes.name

    // Relaciones
    val dietDishes by DietDishEntity referrersOn DietDishes.dishId
    val templateDishes by DietTemplateDishEntity referrersOn DietTemplateDishes.dishId

    fun toModel(): es.gaspardev.core.domain.entities.diets.Dish {
        return es.gaspardev.core.domain.entities.diets.Dish(
            id = this.id.value,
            name = this.name
        )
    }
}

class DietEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DietEntity>(Diets)

    var name by Diets.name
    var description by Diets.description
    var dietType by Diets.dietType
    var duration by Diets.duration
    var createdBy by TrainerEntity referencedOn Diets.createdBy
    var startAt by Diets.startAt

    // Relaciones
    val dishes by DietDishEntity referrersOn DietDishes.dietId
    val athletes by AthleteEntity optionalReferrersOn Athletes.dietId
    val completions by CompletionDietStatisticEntity referrersOn CompletionDietStatistics.dietId
    val notes by NoteEntity via DietNotes

    fun toModel(): Diet {
        return Diet(
            name = this.name,
            description = this.description,
            dietType = this.dietType,
            duration = this.duration,
            dishes = this.dishes.map { it.toModel() }.groupBy({ it.first }, { it.second })
                .mapValues { it.value.toMutableList() }.toMutableMap(),
            startAt = this.startAt
        )
    }
}

class DietDishEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DietDishEntity>(DietDishes)

    var diet by DietEntity referencedOn DietDishes.dietId
    var dish by DishEntity referencedOn DietDishes.dishId
    var weekDay by DietDishes.weekDay
    var amount by DietDishes.amount
    var mealType by DietDishes.mealType

    fun toModel(): Pair<WeekDay, DietDish> {
        return Pair(
            this.weekDay,
            DietDish(
                amout = this.amount,
                mealType = this.mealType,
                dish = this.dish.toModel()
            )
        )
    }
}

class DietTemplateEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DietTemplateEntity>(DietTemplates)

    var name by DietTemplates.name
    var description by DietTemplates.description
    var dietType by DietTemplates.dietType
    var createdBy by TrainerEntity referencedOn DietTemplates.createdBy

    // Relaciones
    val dishes by DietTemplateDishEntity referrersOn DietTemplateDishes.templateId

    fun toModel(): DietTemplate {
        return DietTemplate(
            templateId = this.id.value,
            name = this.name,
            description = this.description,
            dietType = this.dietType,
            dishes = this.dishes.map { it.toModel() }.groupBy({ it.first }, { it.second })
                .mapValues { it.value.toMutableList() }.toMutableMap(),
        )
    }
}

class DietPlanEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DietPlanEntity>(Diets)

    var name by Diets.name
    var description by Diets.description
    var dietType by Diets.dietType
    var duration by Diets.duration
    var createdBy by TrainerEntity referencedOn Diets.createdBy
    var startAt by Diets.startAt

    // Relaciones
    val dishes by DietDishEntity referrersOn DietDishes.dietId
    val athletes by AthleteEntity optionalReferrersOn Athletes.dietId
    val completions by CompletionDietStatisticEntity referrersOn CompletionDietStatistics.dietId

    fun toModel(): DietPlan {
        return DietPlan(
            dietId = this.id.value,
            name = this.name,
            description = this.description,
            type = this.dietType,
            duration = this.duration,
            frequency = this.dishes.map { it.toModel() }.groupBy({ it.first }, { it.second }).keys.count().toString(),
            asignedCount = this.athletes.count().toInt(),
            dishes = this.dishes.map { it.toModel() }.groupBy({ it.first }, { it.second })
                .mapValues { it.value.toMutableList() }.toMutableMap()
        )
    }
}

class DietTemplateDishEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DietTemplateDishEntity>(DietTemplateDishes)

    var template by DietTemplateEntity referencedOn DietTemplateDishes.templateId
    var dish by DishEntity referencedOn DietTemplateDishes.dishId
    var weekDay by DietTemplateDishes.weekDay
    var amount by DietTemplateDishes.amount
    var mealType by DietTemplateDishes.mealType

    fun toModel(): Pair<WeekDay, DietDish> {
        return Pair(
            weekDay,
            DietDish(
                amout = this.amount,
                mealType = this.mealType,
                dish = this.dish.toModel()
            )
        )
    }
}

class CompletionDietStatisticEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CompletionDietStatisticEntity>(CompletionDietStatistics)

    var diet by DietEntity referencedOn CompletionDietStatistics.dietId
    var athlete by AthleteEntity referencedOn CompletionDietStatistics.athleteId
    var completeAt by CompletionDietStatistics.completeAt

    fun toModel(): es.gaspardev.core.domain.entities.diets.CompletionDietStatistics {
        return es.gaspardev.core.domain.entities.diets.CompletionDietStatistics(
            diet = this.diet.toModel(),
            completeAt = this.completeAt,
            asignedAthlete = this.athlete.toModel().user.id
        )
    }
}
