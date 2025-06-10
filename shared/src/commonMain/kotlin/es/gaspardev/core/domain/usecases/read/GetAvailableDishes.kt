package es.gaspardev.core.domain.usecases.read

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.diets.Dish
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.DietRepositoryImp
import es.gaspardev.interfaces.repositories.DietRepository

class GetAvailableDishes(
    private val repo: DietRepository = DietRepositoryImp()
) : UseCase<List<Dish>, UseCase.None>() {
    override suspend fun run(params: None): Either<Exception, List<Dish>> {
        return repo.getAvailableDishes()
    }
}