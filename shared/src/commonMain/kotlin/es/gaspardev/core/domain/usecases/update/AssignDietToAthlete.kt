package es.gaspardev.core.domain.usecases.update

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.diets.DietPlan
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.DietRepositoryImp
import es.gaspardev.interfaces.repositories.DietRepository

class AssignDietToAthlete(
    private val repo: DietRepository = DietRepositoryImp()
) : UseCase<DietPlan, Pair<DietPlan, Athlete>>() {
    override suspend fun run(params: Pair<DietPlan, Athlete>): Either<Exception, DietPlan> {
        val (dietPlan, athlete) = params
        
        return repo.assignDietToAthlete(dietPlan.dietId, athlete.user.id).foldValue(
            { _ ->
                // Actualizar el contador de asignados
                val updatedPlan = dietPlan.copy(asignedCount = dietPlan.asignedCount + 1)
                Either.Success(updatedPlan)
            },
            { err -> Either.Failure(err) }
        )
    }
}
