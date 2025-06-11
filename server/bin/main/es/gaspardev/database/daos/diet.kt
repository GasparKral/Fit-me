package es.gaspardev.database.daos

import es.gaspardev.core.domain.entities.diets.CompletionDietStatistics
import es.gaspardev.core.domain.entities.diets.DietPlan
import es.gaspardev.core.domain.entities.diets.DietTemplate
import es.gaspardev.database.Diets
import es.gaspardev.database.DietDishes
import es.gaspardev.database.entities.*
import es.gaspardev.enums.DietType
import es.gaspardev.enums.MealType
import es.gaspardev.enums.WeekDay
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.time.Duration


object DietDao {
    fun createDiet(
        name: String,
        description: String,
        dietType: DietType,
        duration: Duration,
        createdBy: Int
    ): DietEntity = transaction {
        DietEntity.new {
            this.name = name
            this.description = description
            this.dietType = dietType
            this.duration = duration
            this.createdBy = TrainerEntity.all().first { it.user.id.value == createdBy }
        }
    }

    fun findDietById(id: Int): DietEntity? = transaction {
        DietEntity.findById(id)
    }

    fun addDishToDiet(
        dietId: Int,
        dishId: Int,
        weekDay: WeekDay,
        amount: Double,
        mealType: MealType
    ): DietDishEntity = transaction {
        DietDishEntity.new {
            this.diet = DietEntity[dietId]
            this.dish = DishEntity[dishId]
            this.weekDay = weekDay
            this.amount = amount
            this.mealType = mealType
        }
    }

    fun getPlans(trainerID: Int): List<DietPlan> = transaction {
        DietPlanEntity.all().filter { it.createdBy.user.id.value == trainerID }.map { it.toModel() }
    }

    fun getTemplates(trainerID: Int): List<DietTemplate> = transaction {
        DietTemplateEntity.all().filter { it.createdBy.user.id.value == trainerID }.map { it.toModel() }
    }

    fun completeDiet(dietId: Int, athleteId: Int): CompletionDietStatisticEntity = transaction {
        CompletionDietStatisticEntity.new {
            this.diet = DietEntity[dietId]
            this.athlete = AthleteEntity[athleteId]
            this.completeAt = Instant.fromEpochMilliseconds(System.currentTimeMillis())
        }
    }

    fun getDietsByType(dietType: DietType): List<DietEntity> = transaction {
        DietEntity.find { Diets.dietType eq dietType }.toList()
    }

    fun getCompletedDietsInTimeRange(
        baseRange: Instant,
        upRange: Instant,
        trainerId: Int
    ): List<CompletionDietStatistics> = transaction {
        CompletionDietStatisticEntity.all().toList()
            .filter { it ->
                it.athlete.trainer?.user?.id?._value = trainerId
                it.completeAt in upRange..baseRange
            }.map { it.toModel() }
    }

    fun updateDiet(dietPlan: DietPlan) = transaction {
        val dietEntity = DietPlanEntity.findById(dietPlan.dietId)

        dietEntity?.let { entity ->
            // Actualizar campos básicos
            entity.name = dietPlan.name
            entity.description = dietPlan.description
            entity.dietType = dietPlan.type
            entity.duration = dietPlan.duration

            // Eliminar platos existentes
            DietDishes.deleteWhere { DietDishes.dietId eq dietPlan.dietId }

            // Agregar nuevos platos
            dietPlan.dishes.forEach { (weekDay, dishes) ->
                dishes.forEach { dietDish ->
                    // Buscar o crear el plato
                    val dishEntity = DishEntity.find {
                        es.gaspardev.database.Dishes.name eq dietDish.dish.name
                    }.firstOrNull() ?: DishEntity.new {
                        name = dietDish.dish.name
                    }

                    // Crear la relación diet-dish
                    DietDishEntity.new {
                        diet = DietEntity.findById(dietPlan.dietId)!!
                        dish = dishEntity
                        this.weekDay = weekDay
                        amount = dietDish.amout
                        mealType = dietDish.mealType
                    }
                }
            }

            entity.toModel()
        }
    }

    fun deleteDiet(dietId: Int): Boolean = transaction {
        try {
            val dietEntity = DietPlanEntity.findById(dietId)

            if (dietEntity != null) {
                // Verificar si hay atletas asignados
                if (dietEntity.athletes.count() > 0) {
                    throw IllegalStateException("No se puede eliminar una dieta que tiene atletas asignados")
                }

                // Eliminar platos relacionados
                DietDishes.deleteWhere { DietDishes.dietId eq dietId }

                // Eliminar la dieta
                dietEntity.delete()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun assignDietToAthlete(dietId: Int, athleteId: Int): Boolean = transaction {
        try {
            val athlete = AthleteEntity.all().firstOrNull { it.userEntity.id.value == athleteId }
            val diet = DietEntity.findById(dietId)

            if (athlete != null && diet != null) {
                athlete.diet = diet
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    fun createDietTemplate(
        name: String,
        description: String,
        dietType: DietType,
        createdBy: Int
    ): DietTemplate = transaction {
        val result = DietTemplateEntity.new {
            this.name = name
            this.description = description
            this.dietType = dietType
            this.createdBy = TrainerEntity.all().first { it.user.id.value == createdBy }
        }

        result.toModel()
    }

    fun deleteDietTemplate(templateId: Int): Boolean = transaction {
        try {
            val template = DietTemplateEntity.findById(templateId)
            if (template != null) {
                // Eliminar platos relacionados del template
                template.dishes.forEach { it.delete() }
                // Eliminar el template
                template.delete()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun findDietTemplateById(id: Int): DietTemplateEntity? = transaction {
        DietTemplateEntity.findById(id)
    }
}
