package es.gaspardev.core.infrastructure.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.dtos.LoginUserInfo
import es.gaspardev.core.domain.dtos.RegisterSportsmanData
import es.gaspardev.core.domain.entities.Sportsman
import es.gaspardev.interfaces.repositories.SportsmanRepository

class SportsmantRepositoryImp : SportsmanRepository {
   override suspend fun registerSportsman(newSportsmanData: RegisterSportsmanData): Either<Exception, Sportsman> {
      TODO("Not yet implemented")
   }

   override suspend fun logIn(expectedUser: LoginUserInfo): Either<Exception, Sportsman> {
      return SportsmanRepository.API.get(
         params = arrayOf(
            "userIdentification=" + expectedUser.userIdetification,
            "userPassword=" + expectedUser.userPassword
         )
      )
   }


}