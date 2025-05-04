package es.gaspardev.core.infrastructure.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.DAOs.RegisterSportsmanData
import es.gaspardev.core.domain.entities.Sportsman
import es.gaspardev.core.domain.entities.User
import es.gaspardev.core.infrastructure.apis.SportsmanAPI
import es.gaspardev.interfaces.repositories.EntitieRepository
import es.gaspardev.interfaces.repositories.SportsmanRepository
import es.gaspardev.interfaces.repositories.UserRepository

class SportsmantRepositoryImp : SportsmanRepository {
   override suspend fun registerSportsman(newSportsmanData: RegisterSportsmanData): Either<Exception, Sportsman> {
      TODO("Not yet implemented")
   }


}