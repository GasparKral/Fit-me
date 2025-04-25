package es.gaspardev.interfaces.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.DAOs.LoginUserInfo
import es.gaspardev.core.domain.entities.User
import es.gaspardev.core.infrastructure.apis.UserAPI

interface UserRepository {

    companion object {
        val API = UserAPI()
    }

    suspend fun logIn(expectedUser: LoginUserInfo): Either<Exception, User>
}