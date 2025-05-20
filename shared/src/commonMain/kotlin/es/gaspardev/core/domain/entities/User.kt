package es.gaspardev.core.domain.entities

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable


@Serializable
data class User(
    val id: Int,
    var name: String,
    // var password: String,
    var email: String,
    val creationTime: Instant,
    var userImage: Resource?,
    var status: UserStatus
) {

    companion object {
        const val URLPATH = "/users"
    }

    /**
     * Obtiene las iniciales del nombre de usuario.
     * Toma la primera letra de cada palabra en el nombre y devuelve las dos primeras iniciales.
     * Si solo hay una palabra, devuelve su primera letra.
     */
    fun getInitials(): String {
        return name
            .split(" ")
            .filter { it.isNotEmpty() }
            .take(2)
            .joinToString(separator = "") { it.first().uppercase() }
    }

}