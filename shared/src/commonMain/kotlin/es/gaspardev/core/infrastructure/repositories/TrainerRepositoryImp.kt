package es.gaspardev.core.infrastructure.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.Trainer
import es.gaspardev.core.infrastructure.apis.TrainerAPI
import es.gaspardev.interfaces.repositories.EntitieRepository

class TrainerRepositoryImp : EntitieRepository<Trainer> {

    private val api = TrainerAPI()
    override suspend fun findByEmail(email: String): Either<Exception, Trainer> {
        TODO("Not yet implemented")
    }

    override suspend fun findAll(): Either<Exception, List<Trainer>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteByEmail(email: String): Either.Failure<Exception>? {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): Either<Exception, List<Trainer>> {
        return api.getList("/getAllTrainners")
    }

    override suspend fun save(param: Trainer): Either<Exception, Trainer> {
        return api.post("/save", param, "Error al guardar y/o modificar entrenador")
    }

}
