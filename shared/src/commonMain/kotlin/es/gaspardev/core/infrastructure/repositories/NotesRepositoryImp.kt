package es.gaspardev.core.infrastructure.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.Note
import es.gaspardev.core.domain.entities.User
import es.gaspardev.interfaces.repositories.NotesRepository

class NotesRepositoryImp : NotesRepository {
    override suspend fun getAllUserNotes(user: User): Either<Exception, List<Note>> {
        TODO("Not yet implemented")
    }

    override suspend fun getWorkoutNotes(workoutId: String): Either<Exception, List<Note>> {
        TODO("Not yet implemented")
    }

    override suspend fun getExerciseNotes(exerciseId: String): Either<Exception, List<Note>> {
        TODO("Not yet implemented")
    }

    override suspend fun createNote(note: Note): Either.Failure<Exception>? {
        TODO("Not yet implemented")
    }

}