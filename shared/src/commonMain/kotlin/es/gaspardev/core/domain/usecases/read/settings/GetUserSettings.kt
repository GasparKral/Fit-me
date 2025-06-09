package es.gaspardev.core.domain.usecases.read.settings

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.settings.UserSettings
import es.gaspardev.core.domain.entities.users.User
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.settings.SettingsRepositoryImp
import es.gaspardev.interfaces.repositories.settings.SettingsRepository

/**
 * Use case para obtener la configuración del usuario
 * 
 * @param repository Repositorio de configuraciones
 */
class GetUserSettings(
    private val repository: SettingsRepository = SettingsRepositoryImp()
) : UseCase<UserSettings, User>() {

    /**
     * Ejecuta la obtención de configuraciones del usuario
     * 
     * @param params Usuario del cual obtener las configuraciones
     * @return Either con el resultado de la operación
     */
    override suspend fun run(params: User): Either<Exception, UserSettings> {
        return repository.getUserSettings(params)
    }
}
