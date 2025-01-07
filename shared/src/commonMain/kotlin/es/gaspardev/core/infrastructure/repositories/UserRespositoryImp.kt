package es.gaspardev.core.infrastructure.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.User
import es.gaspardev.interfaces.repositories.UserRepository

class UserRepositoryImp : UserRepository {
    override suspend fun findByEmail(email: String): Either<Exception, User> {
        TODO("Not yet implemented")
    }

    override suspend fun findAll(): Either<Exception, List<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun save(param: User): Either<Exception, User> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteByEmail(email: String): Either.Failure<Exception>? {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): Either<Exception, List<User>> {
        TODO("Not yet implemented")
    }

}