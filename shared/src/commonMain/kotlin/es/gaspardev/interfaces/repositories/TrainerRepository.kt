package es.gaspardev.interfaces.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.Trainer

interface TrainerRepository : UserRepository {
    suspend fun save(user: Trainer): Either<Exception, Trainer>
    suspend fun update(user: Trainer): Either<Exception, Trainer>
}