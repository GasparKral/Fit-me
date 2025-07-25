package es.gaspardev.interfaces.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.dtos.LoginCredentials
import es.gaspardev.core.domain.entities.comunication.Conversation
import es.gaspardev.core.domain.entities.users.User
import es.gaspardev.core.infrastructure.apis.UserAPI

interface UserRepository<T : Any, T2 : Any> {

    companion object {
        val API = UserAPI()
    }

    suspend fun logIn(expectedUser: LoginCredentials): Either<Exception, Triple<T, List<T2>, List<Conversation>>>
    suspend fun getConversations(user: User): Either<Exception, List<Conversation>>

}