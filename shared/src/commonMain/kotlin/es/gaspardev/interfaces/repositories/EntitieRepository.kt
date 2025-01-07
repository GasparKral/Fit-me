package es.gaspardev.interfaces.repositories

import es.gaspardev.auxliars.Either

interface EntitieRepository<Type> where Type : Any {
    suspend fun findByEmail(email: String): Either<Exception, Type>
    suspend fun findAll(): Either<Exception, List<Type>>
    suspend fun save(param: Type): Either<Exception, Type>
    suspend fun deleteByEmail(email: String): Either.Failure<Exception>?
    suspend fun getAll(): Either<Exception, List<Type>>
}