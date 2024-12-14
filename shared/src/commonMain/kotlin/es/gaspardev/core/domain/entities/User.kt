package es.gaspardev.core.domain.entities

import kotlinx.datetime.Instant

data class User(
    val id: Int,
    var name: String,
    var password: String,
    var email: String,
    val creationTime: Instant,
    var userImage: Resource,
) {


}