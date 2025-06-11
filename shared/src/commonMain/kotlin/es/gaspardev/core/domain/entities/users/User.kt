package es.gaspardev.core.domain.entities.users

import es.gaspardev.core.domain.entities.users.info.UserStatus
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
class User(
    private val _id: Int,
    var fullname: String,
    private val _password: String,
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

    val password get() = _password
    val id get() = _id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as User

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "User(id=$id, fullname='$fullname', email='$email', phone=$phone, creationDate=$creationDate, status=$status)"
    }

}