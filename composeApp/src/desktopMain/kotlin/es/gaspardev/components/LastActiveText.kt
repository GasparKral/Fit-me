package es.gaspardev.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fit_me.composeapp.generated.resources.Res
import fit_me.composeapp.generated.resources.last_connection
import kotlinx.datetime.*
import kotlinx.datetime.format.char
import org.jetbrains.compose.resources.stringResource

@Composable
fun LastActiveText(
    lastActive: Instant,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
    textColor: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
    modifier: Modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
) {
    // Formateamos la fecha y hora
    val formattedTime = lastActive.toLocalDateTime(timeZone).formatDateTime()

    Text(
        text = "${stringResource(Res.string.last_connection)} $formattedTime",
        style = MaterialTheme.typography.caption,
        color = textColor,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

// Extensión para formatear LocalDateTime
fun LocalDateTime.formatDateTime(): String {
    return if (this.date == Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date) {
        // Si es hoy, solo mostramos la hora
        formatTime()
    } else {
        // Si no es hoy, mostramos fecha y hora
        formatDateAndTime()
    }
}

// Formatear solo hora (h:mm a)
fun LocalDateTime.formatTime(): String {
    return LocalDateTime.Format {
        hour(); char(':'); minute(); char(' '); amPmHour()
    }.format(this)
}

// Formatear fecha y hora (dd/MM h:mm a)
fun LocalDateTime.formatDateAndTime(): String {
    return LocalDateTime.Format {
        dayOfMonth(); char('/'); monthNumber(); char(' ');
        hour(); char(':'); minute(); char(' '); amPmHour()
    }.format(this)
}