package es.gaspardev.core.domain.entities.users

import es.gaspardev.core.domain.entities.users.info.Certification
import es.gaspardev.core.domain.entities.users.info.Social
import es.gaspardev.core.domain.entities.users.info.TimeSlot
import es.gaspardev.enums.WeekDay
import kotlinx.serialization.Serializable

@Serializable
class Trainer(
    val user: User,
    var specialization: String,
    var yearsOfExperiencie: Int,
    private val raw_socials: Map<String, String> = mapOf(),
    val certifications: List<Certification> = listOf(),
    val availability: Map<WeekDay, List<TimeSlot>> = mapOf()
) {

    companion object {
        const val URLPATH = "/user/trainer"
    }

    val socials: Map<Social, String>
        get() = raw_socials.mapKeys { (key, _) -> Social.from(key) }

}