package es.gaspardev.core.domain.entities

import es.gaspardev.enums.MediaType
import kotlinx.serialization.Serializable

@Serializable
data class Resource(
    val resourceType: MediaType,
    var src: String
) {

}