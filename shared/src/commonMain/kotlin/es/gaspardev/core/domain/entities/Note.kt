package es.gaspardev.core.domain.entities

data class Note(
    val id: Int,
    val user: User,
    var message: String
) {

}