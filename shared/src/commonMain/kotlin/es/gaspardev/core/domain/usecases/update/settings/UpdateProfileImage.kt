package es.gaspardev.core.domain.usecases.update.settings

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.settings.UserSettings
import es.gaspardev.core.domain.entities.users.User
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.settings.SettingsRepositoryImp
import es.gaspardev.interfaces.repositories.settings.SettingsRepository

/**
 * Use case para actualizar la imagen de perfil del usuario
 * 
 * @param repository Repositorio de configuraciones
 */
class UpdateProfileImage(
    private val repository: SettingsRepository = SettingsRepositoryImp()
) : UseCase<UserSettings, UpdateProfileImageParams>() {

    /**
     * Ejecuta la actualización de la imagen de perfil
     * 
     * @param params Parámetros para la actualización de imagen
     * @return Either con el resultado de la operación
     */
    override suspend fun run(params: UpdateProfileImageParams): Either<Exception, UserSettings> {
        // Validar datos antes de enviar al repositorio
        val validationResult = validateImageData(params)
        if (validationResult != null) {
            return Either.Failure(validationResult)
        }
        
        return repository.updateProfileImage(
            user = params.user,
            imageUrl = params.imageUrl
        )
    }

    /**
     * Valida los datos de la imagen antes de procesar la actualización
     * 
     * @param params Parámetros a validar
     * @return Exception si hay errores de validación, null si todo es válido
     */
    private fun validateImageData(params: UpdateProfileImageParams): Exception? {
        // Validar que la URL no esté vacía
        if (params.imageUrl.isBlank()) {
            return IllegalArgumentException("La URL de la imagen no puede estar vacía")
        }
        
        // Validar formato de URL
        if (!isValidUrl(params.imageUrl)) {
            return IllegalArgumentException("La URL de la imagen no tiene un formato válido")
        }
        
        // Validar que sea una URL de imagen
        if (!isImageUrl(params.imageUrl)) {
            return IllegalArgumentException("La URL debe apuntar a un archivo de imagen válido (jpg, jpeg, png, gif, webp)")
        }
        
        // Validar longitud de URL (evitar URLs extremadamente largas)
        if (params.imageUrl.length > 2048) {
            return IllegalArgumentException("La URL de la imagen es demasiado larga")
        }
        
        return null
    }
    
    /**
     * Valida que sea una URL válida
     */
    private fun isValidUrl(url: String): Boolean {
        val urlRegex = Regex("^https?://[\\w\\-]+(\\.[\\w\\-]+)+[/#?]?.*$")
        return urlRegex.matches(url)
    }
    
    /**
     * Verifica que la URL apunte a un archivo de imagen
     */
    private fun isImageUrl(url: String): Boolean {
        val imageExtensions = listOf(".jpg", ".jpeg", ".png", ".gif", ".webp", ".bmp", ".svg")
        val lowerUrl = url.lowercase()
        
        // Verificar extensión en la URL
        val hasImageExtension = imageExtensions.any { extension ->
            lowerUrl.contains(extension)
        }
        
        // Verificar parámetros de query que indiquen imagen
        val hasImageParams = lowerUrl.contains("format=") && 
                            (lowerUrl.contains("jpg") || lowerUrl.contains("png") || lowerUrl.contains("webp"))
        
        return hasImageExtension || hasImageParams
    }
    
    companion object {
        // Extensiones de imagen soportadas
        val SUPPORTED_IMAGE_EXTENSIONS = listOf(
            "jpg", "jpeg", "png", "gif", "webp", "bmp", "svg"
        )
        
        // Tamaño máximo recomendado para imágenes de perfil (en caracteres de URL)
        const val MAX_URL_LENGTH = 2048
        
        // URLs de placeholder por defecto para cuando no hay imagen
        val DEFAULT_AVATAR_URLS = mapOf(
            "male" to "https://ui-avatars.com/api/?name=User&background=2196F3&color=fff&size=200",
            "female" to "https://ui-avatars.com/api/?name=User&background=E91E63&color=fff&size=200",
            "default" to "https://ui-avatars.com/api/?name=User&background=607D8B&color=fff&size=200"
        )
        
        /**
         * Genera una URL de avatar por defecto basada en el nombre del usuario
         */
        fun generateDefaultAvatar(userName: String, gender: String? = null): String {
            val name = userName.replace(" ", "+")
            val background = when (gender?.lowercase()) {
                "male" -> "2196F3"
                "female" -> "E91E63"
                else -> "607D8B"
            }
            return "https://ui-avatars.com/api/?name=$name&background=$background&color=fff&size=200"
        }
    }
}

/**
 * Parámetros para la actualización de imagen de perfil
 */
data class UpdateProfileImageParams(
    val user: User,
    val imageUrl: String
)
