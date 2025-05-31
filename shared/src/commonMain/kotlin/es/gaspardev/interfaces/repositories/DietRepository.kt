package es.gaspardev.interfaces.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.diets.CompletionDietStatistics
import es.gaspardev.core.domain.entities.diets.Diet
import es.gaspardev.core.domain.entities.diets.DietPlan
import es.gaspardev.core.domain.entities.diets.DietTemplate
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

    suspend fun getDietsHistory(athlete: Athlete): Either<Exception, List<CompletionDietStatistics>>
    suspend fun createDiet(diet: Diet, trainer: Trainer): Either<Exception, Boolean>
}