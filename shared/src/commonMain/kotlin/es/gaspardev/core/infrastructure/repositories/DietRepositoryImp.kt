package es.gaspardev.core.infrastructure.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.diets.*
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

    override suspend fun getDietById(dietId: Int): Either<Exception, DietPlan> {
        return DietRepository.API.getSingleValue<DietPlan>(
            listOf("plan", dietId.toString())
        )
    }

    override suspend fun getDietsHistory(athlete: Athlete): Either<Exception, List<CompletionDietStatistics>> {
        return DietRepository.API.getGenericList<CompletionDietStatistics>(
            listOf("data", "diethitory", athlete.user.id.toString())
        )
    }


    override suspend fun createDiet(diet: Diet, trainer: Trainer): Either<Exception, Int> {
        return DietRepository.API.postGeneric<Diet, Diet>(
            listOf("create", trainer.user.id.toString()),
            diet
        ).foldValue(
            { value -> Either.Success(value.getId()) },
            { err -> Either.Failure(err) }
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

    override suspend fun assignDietToAthlete(dietId: Int, athleteId: Int): Either<Exception, Unit> {
        return DietRepository.API.patch(
            listOf("assign"),
            mapOf(
                "dietId" to dietId,
                "athleteId" to athleteId
            )
        ).foldValue(
            { _ -> Either.Success(Unit) },
            { err -> Either.Failure(err) }
        )
    }

    override suspend fun createDietTemplate(template: DietTemplate, trainer: Trainer): Either<Exception, Int> {
        return DietRepository.API.postGeneric<DietTemplate, DietTemplate>(
            listOf("templates", "create", trainer.user.id.toString()),
            template
        ).foldValue(
            { value -> Either.Success(value.getId()) },
            { err -> Either.Failure(err) }
        )
    }

    override suspend fun deleteDietTemplate(templateId: Int): Either<Exception, Unit> {
        return DietRepository.API.delete(
            listOf("templates"),
            params = arrayOf(Pair("template_id", templateId.toString()))
        )
    }

    override suspend fun getAvailableDishes(): Either<Exception, List<Dish>> {
        return DietRepository.API.getGenericList(listOf("dishes"))
    }

}
