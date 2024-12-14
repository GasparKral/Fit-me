package es.gaspardev.core.domain.entities

data class Dish(
    val id: Int,
    var name: String,
    val author: Trainner?
) {

}