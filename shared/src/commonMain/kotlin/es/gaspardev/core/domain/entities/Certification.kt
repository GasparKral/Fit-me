package es.gaspardev.core.domain.entities

import kotlinx.datetime.Instant

data class Certification(
    val id: Int,
    var name: String,
    var issuinOrganization: String,
    val dateObtained: Instant,
) {

}