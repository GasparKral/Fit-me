package es.gaspardev.core.domain.entities

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Certification(
    var name: String,
    var issuinOrganization: String,
    val dateObtained: Instant,
) {

}