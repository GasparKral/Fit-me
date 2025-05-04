package es.gaspardev.core.infrastructure.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.DAOs.LoginUserInfo
import es.gaspardev.core.domain.entities.User
import es.gaspardev.interfaces.repositories.UserRepository

class UserRepositoryImp : UserRepository {
    override suspend fun logIn(expectedUser: LoginUserInfo): Either<Exception, User> {
        return UserRepository.API.get(
            params = arrayOf(
                "userIdentification=" + expectedUser.userIdetification,
                "userPassword=" + expectedUser.userPassword
            )
        )

    }

}