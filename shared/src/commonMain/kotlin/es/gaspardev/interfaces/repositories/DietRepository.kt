package es.gaspardev.interfaces.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.diets.*
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.infrastructure.apis.DietAPI

interface DietRepository {

    companion object {
        val API = DietAPI()
    }

    suspend fun getDiets(trainer: Trainer): Either<Exception, List<Diet>>
    suspend fun getDietsPlans(trainer: Trainer): Either<Exception, List<DietPlan>>
    suspend fun getDietsTemplates(trainer: Trainer): Either<Exception, List<DietTemplate>>
    suspend fun getDietById(dietId: Int): Either<Exception, DietPlan>

    suspend fun getDietsHistory(athlete: Athlete): Either<Exception, List<CompletionDietStatistics>>

    suspend fun createDiet(diet: Diet, trainer: Trainer): Either<Exception, Int>
    suspend fun deleteDiet(dietId: Int): Either<Exception, Unit>
    suspend fun updateDiet(dietPlan: DietPlan): Either<Exception, DietPlan>
    suspend fun assignDietToAthlete(dietId: Int, athleteId: Int): Either<Exception, Unit>
    suspend fun createDietTemplate(template: DietTemplate, trainer: Trainer): Either<Exception, Int>
    suspend fun deleteDietTemplate(templateId: Int): Either<Exception, Unit>
    suspend fun getAvailableDishes(): Either<Exception, List<Dish>>
}