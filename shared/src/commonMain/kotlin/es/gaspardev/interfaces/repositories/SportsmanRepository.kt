package es.gaspardev.interfaces.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.dtos.RegisterSportsmanData
import es.gaspardev.core.domain.entities.Sportsman
import es.gaspardev.core.infrastructure.apis.SportsmanAPI

interface SportsmanRepository : UserRepository<Sportsman> {

    companion object {
        val API = SportsmanAPI()
    }

    suspend fun registerSportsman(newSportsmanData: RegisterSportsmanData): Either<Exception, Sportsman>

}