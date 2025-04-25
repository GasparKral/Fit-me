package es.gaspardev.interfaces.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.DAOs.RegisterSportsmanData
import es.gaspardev.core.domain.entities.Sportsman
import es.gaspardev.core.infrastructure.apis.SportsmanAPI

interface SportsmanRepository {

    companion object {
        val API = SportsmanAPI()
    }

    suspend fun registerSportsman(newSportsmanData: RegisterSportsmanData): Either<Exception, Sportsman>

}