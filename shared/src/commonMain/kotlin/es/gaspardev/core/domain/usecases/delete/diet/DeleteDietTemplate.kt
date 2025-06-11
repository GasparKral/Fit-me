package es.gaspardev.core.domain.usecases.delete.diet

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.DietRepositoryImp
import es.gaspardev.interfaces.repositories.DietRepository

/**
 * Use case para eliminar una plantilla de dieta
 * 
 * @param repo Repositorio de dietas
 */
class DeleteDietTemplate(
    private val repo: DietRepository = DietRepositoryImp()
) : UseCase<UseCase.None, Int>() {
    
    /**
     * Ejecuta la eliminación de la plantilla de dieta
     * 
     * @param params ID de la plantilla a eliminar
     * @return Either con el resultado de la operación
     */
    override suspend fun run(params: Int): Either<Exception, None> {
        // Validar que el ID sea válido
        if (params <= 0) {
            return Either.Failure(IllegalArgumentException("El ID de la plantilla debe ser mayor a 0"))
        }
        
        return repo.deleteDietTemplate(params).foldValue(
            { _ -> Either.Success(None) },
            { err -> Either.Failure(err) }
        )
    }
}
