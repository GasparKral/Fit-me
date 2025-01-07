package es.gaspardev.interfaces.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.Sportsman

interface SportsmanRepository : UserRepository {
    suspend fun save(user: Sportsman): Either<Exception, Sportsman>
    suspend fun update(user: Sportsman): Either<Exception, Sportsman>
}