package es.gaspardev.core.domain.DAOs

import es.gaspardev.core.domain.entities.Social
import es.gaspardev.core.domain.entities.WeeklyAvailability
import kotlinx.serialization.Serializable

@Serializable
data class TrainerPatchDTO(
    val specialization: String? = null,
    val yearsOfExperience: Int? = null,
    val bio: String? = null,
    val availability: WeeklyAvailability? = null,
    val socialLinks: Map<Social, String>?
) {

}
