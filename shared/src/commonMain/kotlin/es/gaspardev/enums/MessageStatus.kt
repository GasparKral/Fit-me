package es.gaspardev.enums

import kotlinx.serialization.Serializable

@Serializable
enum class MessageStatus {
    SENT, DELIVERED, READ
}