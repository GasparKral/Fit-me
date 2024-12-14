package es.gaspardev.core.domain.entities

import es.gaspardev.enums.BodyPart

data class Exercise(
    val id: Int,
    val name: String,
    val bodyPart: BodyPart,
    var description: String,
    val author: Trainner,
    var video: Resource?
) {

}