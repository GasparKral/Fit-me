package es.gaspardev.interfaces.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.dtos.LoginUserInfo
import es.gaspardev.core.infrastructure.apis.UserAPI

interface UserRepository<T : Any> {

    companion object {
        val API = UserAPI()
    }

    suspend fun logIn(expectedUser: LoginUserInfo): Either<Exception, T>

}