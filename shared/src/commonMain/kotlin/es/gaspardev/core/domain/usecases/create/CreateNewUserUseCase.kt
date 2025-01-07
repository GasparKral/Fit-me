package es.gaspardev.core.domain.usecases.create

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.User
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.UserRepositoryImp

class CreateNewUserUseCase(
    private val userRepository: UserRepositoryImp
) : UseCase<User, User>() {
    override suspend fun run(params: User): Either<Exception, User> {
        return userRepository.save(params)
    }

}