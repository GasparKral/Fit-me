package es.gaspardev.core.domain.entities.comunication

import es.gaspardev.core.domain.entities.Note
import es.gaspardev.enums.SessionType
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class Session(
    var title: String,
    var dateTime: Instant,
    var sessionType: SessionType,
    val with: String, // Athelete name
    var expectedDuration: Duration
)