package es.gaspardev.core.domain.entities

import kotlinx.serialization.Serializable

@Serializable
class Trainer(
    val user: User,
    var specialization: String,
    var yearsOfExperience: Int,
    var bio: String,
    val socialLinks: Map<Social, String>,
    var availability: WeeklyAvailability,
    val certifications: List<Certification>,
    val sportmans: List<Sportsman>
) {

    companion object {
        const val URLPATH = "/users/trainer"
    }

}