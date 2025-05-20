package es.gaspardev.core.domain.usecases.read

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.dtos.LoginUserInfo
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.interfaces.repositories.UserRepository


class LogInUser<T : Any>(
    private val clientRepository: UserRepository<T>
) : UseCase<T, LoginUserInfo>() {
    override suspend fun run(params: LoginUserInfo): Either<Exception, T> {
        return clientRepository.logIn(params)
    }
}