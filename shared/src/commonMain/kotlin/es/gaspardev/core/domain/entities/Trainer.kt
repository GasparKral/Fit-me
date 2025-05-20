package es.gaspardev.core.domain.entities

import kotlinx.serialization.Serializable

@Serializable
class Trainer(
    val user: User,
    var specialization: String,
    var yearsOfExperience: Int,
    var bio: String,
    private var _rawSocialLinks: Map<String, String>,  // Private backing property
    var availability: WeeklyAvailability,
    val certifications: List<Certification>,
    val sportmans: List<Sportsman>
) {
    companion object {
        const val URLPATH = "/users/trainer"
    }

    // Public property with custom getter (not in primary constructor)
    val socialLinks: Map<Social, String>
        get() = _rawSocialLinks.mapKeys { (key, _) -> Social.valueOf(key) }

    // Function to update social links
    fun updateSocialLinks(newLinks: Map<String, String>) {
        _rawSocialLinks = newLinks
    }

    // Property for serialization (same type as constructor parameter)
    val rawSocialLinks: Map<String, String>
        get() = _rawSocialLinks
}