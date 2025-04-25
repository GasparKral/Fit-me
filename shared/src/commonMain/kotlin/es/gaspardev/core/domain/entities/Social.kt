package es.gaspardev.core.domain.entities

import kotlinx.serialization.Serializable

@Serializable
sealed class Social {
    object Instagram : Social()
    object X_teeter : Social()
    object Linkeding : Social()
    object Facebook : Social()
}
