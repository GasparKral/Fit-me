package es.gaspardev.core.domain.entities.users

import es.gaspardev.core.domain.entities.users.info.TimeSlot
import es.gaspardev.enums.WeekDay
import kotlinx.serialization.Serializable

@Serializable
class Trainer(
    val user: User,
    var specialization: String,
    var yearsOfExperiencie: Int,
    val availability: Map<WeekDay, List<TimeSlot>> = mapOf()
) {

    companion object {
        const val URLPATH = "/user/trainer"
    }

}