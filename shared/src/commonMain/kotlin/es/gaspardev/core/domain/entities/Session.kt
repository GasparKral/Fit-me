package es.gaspardev.core.domain.entities

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class Session(
    val id: Int,
    val dateTime: Instant,
    val with: Sportsman,
    val duration: Duration
) {}
