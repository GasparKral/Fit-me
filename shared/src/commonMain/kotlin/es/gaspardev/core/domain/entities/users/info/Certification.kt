package es.gaspardev.core.domain.entities.users.info

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Certification(
    val id: Int,
    val name: String,
    var issuingOrganization: String,
    val completeAt: Instant
) 