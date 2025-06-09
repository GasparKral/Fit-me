package es.gaspardev.core.domain.usecases.create

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.diets.DietPlan
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.DietRepositoryImp
import es.gaspardev.interfaces.repositories.DietRepository

class DuplicateDiet(
    private val repo: DietRepository = DietRepositoryImp()
) : UseCase<DietPlan, Triple<DietPlan, String, Trainer>>() {
    override suspend fun run(params: Triple<DietPlan, String, Trainer>): Either<Exception, DietPlan> {
        val (originalDiet, newName, trainer) = params
        
        // Crear una nueva dieta basada en la original
        val newDiet = es.gaspardev.core.domain.entities.diets.Diet(
            name = newName,
            description = "${originalDiet.description} (Copia)",
            dietType = originalDiet.type,
            duration = originalDiet.duration,
            dishes = originalDiet.dishes.toMutableMap()
        )
        
        return repo.createDiet(newDiet, trainer).foldValue(
            { dietId ->
                Either.Success(
                    DietPlan(
                        dietId = dietId,
                        name = newDiet.name,
                        description = newDiet.description,
                        type = newDiet.dietType,
                        duration = newDiet.duration,
                        frequency = originalDiet.frequency,
                        asignedCount = 0,
                        dishes = newDiet.dishes
                    )
                )
            },
            { err -> Either.Failure(err) }
        )
    }
}
