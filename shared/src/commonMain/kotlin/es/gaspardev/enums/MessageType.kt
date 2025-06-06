package es.gaspardev.enums

import kotlinx.serialization.Serializable

@Serializable
enum class MessageType {
    TEXT,
    IMAGE,
    FILE,
    AUDIO,
    VIDEO,
    SYSTEM
}