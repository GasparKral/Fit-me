package es.gaspardev.core.infrastructure.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.Sportsman
import es.gaspardev.core.domain.entities.User
import es.gaspardev.core.infrastructure.apis.SportsmanAPI
import es.gaspardev.interfaces.repositories.EntitieRepository
import es.gaspardev.interfaces.repositories.SportsmanRepository

class SportsmantRepositoryImp : SportsmanRepository {

    private val api = SportsmanAPI();
    override suspend fun save(user: Sportsman): Either<Exception, Sportsman> {
        TODO("Not yet implemented")
    }

    override suspend fun save(param: User): Either<Exception, User> {
        TODO("Not yet implemented")
    }

    override suspend fun update(user: Sportsman): Either<Exception, Sportsman> {
        TODO("Not yet implemented")
    }

    override suspend fun findByEmail(email: String): Either<Exception, User> {
        TODO("Not yet implemented")
    }

    override suspend fun findAll(): Either<Exception, List<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteByEmail(email: String): Either.Failure<Exception>? {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): Either<Exception, List<User>> {
        TODO("Not yet implemented")
    }


}