package es.gaspardev.core.domain.entities.users

import es.gaspardev.core.domain.entities.users.info.UserStatus
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
class User(
    val id: Int,
    var fullname: String,
    private var password: String,
    var email: String,
    var phone: String?,
    val creationDate: Instant,
    var status: UserStatus
) {

    companion object {
        val URLPATH = "/users"
    }

    fun getInitials(): String {
        return fullname
            .split(" ")
            .filter { it.isNotEmpty() }
            .take(2)
            .joinToString(separator = "") { it.first().uppercase() }
    }

    fun getPassword(): String {
        return this.password
    }

}