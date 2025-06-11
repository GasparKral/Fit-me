package es.gaspardev.core.domain.usecases.read.user

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.comunication.Conversation
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.domain.entities.users.User
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.interfaces.repositories.UserRepository

class GetConversations(
    private val repo: UserRepository<Trainer, Athlete>
) : UseCase<List<Conversation>, User>() {
    override suspend fun run(params: User): Either<Exception, List<Conversation>> {
        return repo.getConversations(params)
    }
}