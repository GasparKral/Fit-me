package es.gaspardev.core.domain.entities.users.info

sealed class Social {
    object Instagram : Social()
    object Twitter : Social()
    object Facebook : Social()

    companion object {
        fun from(key: String): Social {
            return when (key.lowercase()) {
                "instagram" -> Instagram
                "twitter" -> Twitter
                else -> Facebook
            }
        }
    }
}