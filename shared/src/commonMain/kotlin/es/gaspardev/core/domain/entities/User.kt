package es.gaspardev.core.domain.entities

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable


@Serializable
data class User(
    val id: Int,
    var name: String,
    var password: String,
    var email: String,
    val creationTime: Instant,
    var userImage: Resource?,
) {

    companion object {
        const val URLPATH = "/users"
    }

}