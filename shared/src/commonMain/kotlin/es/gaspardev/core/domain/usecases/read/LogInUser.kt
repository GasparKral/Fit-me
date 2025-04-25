package es.gaspardev.core.domain.usecases.read

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.DAOs.LoginUserInfo
import es.gaspardev.core.domain.entities.User
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.interfaces.repositories.UserRepository


class LogInUser(
    private val userRepository: UserRepository
) : UseCase<User, LoginUserInfo>() {
    override suspend fun run(params: LoginUserInfo): Either<Exception, User> {
        return userRepository.logIn(
            params
        )
    }

}