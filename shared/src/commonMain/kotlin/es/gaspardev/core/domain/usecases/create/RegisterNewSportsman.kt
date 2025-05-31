package es.gaspardev.core.domain.usecases.create

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.dtos.RegisterAthleteData

import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.AthletetRepositoryImp

class RegisterNewSportsman(
    private val sportsmanRepository: AthletetRepositoryImp
) : UseCase<Athlete, RegisterAthleteData>() {
    override suspend fun run(params: RegisterAthleteData): Either<Exception, Athlete> {
        return sportsmanRepository.registerAthlete(params)
    }
}