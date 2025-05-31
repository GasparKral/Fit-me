package es.gaspardev.core.domain.entities.users.info

import es.gaspardev.enums.StatusState
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class UserStatus(
    val state: StatusState = StatusState.INACTIVE,
    var lastTimeActive: Instant
)