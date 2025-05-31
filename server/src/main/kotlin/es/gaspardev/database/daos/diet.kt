package es.gaspardev.database.daos

import es.gaspardev.core.domain.entities.diets.CompletionDietStatistics
import es.gaspardev.core.domain.entities.diets.DietPlan
import es.gaspardev.core.domain.entities.diets.DietTemplate
import es.gaspardev.database.Diets
import es.gaspardev.database.entities.*
import es.gaspardev.enums.DietType
import es.gaspardev.enums.MealType
import es.gaspardev.enums.WeekDay
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.time.Duration


class DietDao {
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

    fun getPlans(trainerID: String): List<DietPlan> = transaction {
        DietPlanEntity.all().filter { it ->
            it.athletes.any { it.trainer?.id?.value == trainerID.toInt() }
        }.map { it.toModel() }
    }

    fun getTemplates(trainerID: String): List<DietTemplate> = transaction {
        DietTemplateEntity.all().filter { it.createdBy.user.id.value == trainerID.toInt() }.map { it.toModel() }
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
        trainerId: String
    ): List<CompletionDietStatistics> = transaction {
        CompletionDietStatisticEntity.all().toList()
            .filter { it ->
                it.athlete.trainer?.user?.id?._value = trainerId.toInt()
                it.completeAt in upRange..baseRange
            }.map { it.toModel() }
    }
}
