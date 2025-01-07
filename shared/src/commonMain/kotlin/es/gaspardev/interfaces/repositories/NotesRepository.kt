package es.gaspardev.interfaces.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.Note
import es.gaspardev.core.domain.entities.User

interface NotesRepository {

    suspend fun getAllUserNotes(user: User): Either<Exception, List<Note>>

    suspend fun getWorkoutNotes(workoutId: String): Either<Exception, List<Note>>

    suspend fun getExerciseNotes(exerciseId: String): Either<Exception, List<Note>>

    suspend fun createNote(note: Note): Either.Failure<Exception>?
}