package es.gaspardev.core.domain.entities

data class Chat(
    val id: Int,
    val trainner: Trainner,
    val sportman: Sportman,
    val message: List<Message>
) {

}