package es.gaspardev.core.domain.usecases.delete.workout

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.WorkoutRepositoryImp
import es.gaspardev.interfaces.repositories.WorkoutRepository

/**
 * Use case para eliminar una plantilla de entrenamiento
 * 
 * @param repo Repositorio de entrenamientos
 */
class DeleteWorkoutTemplate(
    private val repo: WorkoutRepository = WorkoutRepositoryImp()
) : UseCase<UseCase.None, Int>() {
    
    /**
     * Ejecuta la eliminación de la plantilla de entrenamiento
     * 
     * @param params ID de la plantilla a eliminar
     * @return Either con el resultado de la operación
     */
    override suspend fun run(params: Int): Either<Exception, None> {
        // Validar que el ID sea válido
        if (params <= 0) {
            return Either.Failure(IllegalArgumentException("El ID de la plantilla debe ser mayor a 0"))
        }
        
        return repo.deleteWorkoutTemplate(params).foldValue(
            { _ -> Either.Success(None) },
            { err -> Either.Failure(err) }
        )
    }
}
