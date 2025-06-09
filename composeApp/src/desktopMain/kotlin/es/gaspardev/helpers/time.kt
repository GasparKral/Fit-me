package es.gaspardev.helpers

import kotlinx.datetime.*
import kotlinx.datetime.format.char
import kotlin.time.Duration
import kotlin.time.DurationUnit

fun Instant.toSpainDate(): String {
    return this.toLocalDateTime(TimeZone.currentSystemDefault()).date.format(
        LocalDate.Format {
            dayOfMonth();char('/');monthNumber();char('/');year()
        }
    )
}

fun Duration.toSpainTime(unit: DurationUnit = DurationUnit.HOURS): String {
    val totalSeconds = this.toLong(DurationUnit.SECONDS)

    return when (unit) {
        DurationUnit.HOURS -> {
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60
            val seconds = totalSeconds % 60
            "${hours}h ${minutes}m ${seconds}s"
        }

        DurationUnit.MINUTES -> {
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            "${minutes}m ${seconds}s"
        }

        DurationUnit.SECONDS -> {
            "${totalSeconds}s"
        }

        DurationUnit.MILLISECONDS -> {
            val totalMillis = this.toLong(DurationUnit.MILLISECONDS)
            "${totalMillis}ms"
        }

        else -> {
            // Para otros casos, convertir a horas:minutos:segundos
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60
            val seconds = totalSeconds % 60
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }
    }
}