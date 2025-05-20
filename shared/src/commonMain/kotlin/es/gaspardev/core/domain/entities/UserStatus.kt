package es.gaspardev.core.domain.entities

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class UserStatus(
    val status: Boolean = false, // 1 = activo,0 = inactivo
    val lastActive: kotlinx.datetime.Instant = Clock.System.now(),
    val needsAttetion: Boolean = false
) {
    fun getLastConectionTimeDiference(): Duration {
        return Clock.System.now().minus(lastActive)
    }
}
