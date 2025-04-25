package es.gaspardev.core.domain.usecases.create

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.DAOs.RegisterSportsmanData
import es.gaspardev.core.domain.entities.Sportsman
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.interfaces.repositories.SportsmanRepository

class RegisterNewSportsman(
    private val sportsmanRepository: SportsmanRepository
) : UseCase<Sportsman, RegisterSportsmanData>() {
    override suspend fun run(params: RegisterSportsmanData): Either<Exception, Sportsman> {
        return sportsmanRepository.registerSportsman(params)
    }
}