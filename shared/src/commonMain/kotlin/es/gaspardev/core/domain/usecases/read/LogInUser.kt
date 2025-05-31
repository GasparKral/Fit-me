package es.gaspardev.core.domain.usecases.read

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.dtos.LoginCredentials
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.interfaces.repositories.UserRepository


class LogInUser<T : Any, T2 : Any>(
    private val clientRepository: UserRepository<T, T2>
) : UseCase<Pair<T, List<T2>>, LoginCredentials>() {
    override suspend fun run(params: LoginCredentials): Either<Exception, Pair<T, List<T2>>> {
        return clientRepository.logIn(params)
    }
}