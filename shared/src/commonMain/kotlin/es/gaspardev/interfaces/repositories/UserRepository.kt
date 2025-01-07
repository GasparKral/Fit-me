package es.gaspardev.interfaces.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.User

interface UserRepository : EntitieRepository<User> {
    override suspend fun findByEmail(email: String): Either<Exception, User>
    override suspend fun findAll(): Either<Exception, List<User>>
    override suspend fun save(param: User): Either<Exception, User> // Se usa tanto para guardar cambios como para guardar un nuevo usuario
    override suspend fun deleteByEmail(email: String): Either.Failure<Exception>?
}