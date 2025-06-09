package es.gaspardev.core.infrastructure.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.diets.CompletionDietStatistics
import es.gaspardev.core.domain.entities.diets.Diet
import es.gaspardev.core.domain.entities.diets.DietPlan
import es.gaspardev.core.domain.entities.diets.DietTemplate
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.interfaces.repositories.DietRepository

class DietRepositoryImp : DietRepository {
    override suspend fun getDiets(trainer: Trainer): Either<Exception, List<Diet>> {
        TODO("Not yet implemented")
    }

    override suspend fun getDietsPlans(trainer: Trainer): Either<Exception, List<DietPlan>> {
        return DietRepository.API.getGenericList<DietPlan>(
            listOf("plans", trainer.user.id.toString())
        )
    }

    override suspend fun getDietsTemplates(trainer: Trainer): Either<Exception, List<DietTemplate>> {
        return DietRepository.API.getGenericList<DietTemplate>(
            listOf("templates", trainer.user.id.toString())
        )
    }

    override suspend fun getDietsHistory(athlete: Athlete): Either<Exception, List<CompletionDietStatistics>> {
        return DietRepository.API.getGenericList<CompletionDietStatistics>(
            listOf("data", "history"),
            params = arrayOf(Pair("trainer_id", athlete.user.id.toString()))
        )
    }

    override suspend fun createDiet(diet: Diet, trainer: Trainer): Either<Exception, Int> {
        return DietRepository.API.post(
            listOf("create"),
            Pair(diet, trainer)
        ).foldValue<Either<Exception, Int>>(
            { value -> return Either.Success(value.getId()) },
            { err -> return Either.Failure(err) }
        )
    }

    override suspend fun deleteDiet(dietId: Int): Either<Exception, Unit> {
        return DietRepository.API.delete(emptyList(), params = arrayOf(Pair("diet_id", dietId.toString())))
    }

    override suspend fun updateDiet(dietPlan: DietPlan): Either<Exception, DietPlan> {
        return DietRepository.API.patch(emptyList(), dietPlan).foldValue(
            { _ -> Either.Success(dietPlan) },
            { err -> Either.Failure(err) }
        )
    }

}
