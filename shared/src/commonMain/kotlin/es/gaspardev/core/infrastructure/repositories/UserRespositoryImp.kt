package es.gaspardev.core.infrastructure.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.DAOs.LoginUserInfo
import es.gaspardev.core.domain.entities.User
import es.gaspardev.interfaces.repositories.UserRepository

class UserRepositoryImp : UserRepository {
    override suspend fun logIn(expectedUser: LoginUserInfo): Either<Exception, User> {
        val request = UserRepository.API.get(
            params = arrayOf(
                "userIdentification=" + expectedUser.userIdetification,
                "userPassword=" + expectedUser.userPassword
            )
        )
        when (request) {
            is Either.Failure -> return Either.Failure(request.error)
            is Either.Success -> return Either.Success(request.value[0])
        }
    }

    override suspend fun updateTrainerInfo(): Either<Exception, Void> {
        TODO("Not yet implemented")
    }


}