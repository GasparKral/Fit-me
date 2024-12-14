package es.gaspardev.core.domain.entities

data class Trainner(
    val user: User,
    var specialization: String,
    var yearsOfExperience: Int,
    var bio: String,
    val socialLinks: Map<String, String>,
    var availability: String,
    var rating: Double,
    val certifications: List<Certification>
) {

}