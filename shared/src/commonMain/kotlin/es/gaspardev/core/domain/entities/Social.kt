package es.gaspardev.core.domain.entities

import kotlinx.serialization.Serializable

@Serializable
sealed class Social {
    companion object {
        fun valueOf(socialMedia: String): Social {
            return when (socialMedia) {
                "Instagram" -> Instagram
                "X_teeter" -> X_teeter
                "Linkeding" -> Linkeding
                "Facebook" -> Facebook
                else -> None
            }
        }
    }

    @Serializable
    object Instagram : Social()

    @Serializable
    object X_teeter : Social()

    @Serializable
    object Linkeding : Social()

    @Serializable
    object Facebook : Social()

    @Serializable
    object None : Social()
}
