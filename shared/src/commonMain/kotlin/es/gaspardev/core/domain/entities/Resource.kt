package es.gaspardev.core.domain.entities

import es.gaspardev.enums.MediaType

data class Resource(
    val id: Int,
    val resourceType: MediaType,
    var src: String
) {

}