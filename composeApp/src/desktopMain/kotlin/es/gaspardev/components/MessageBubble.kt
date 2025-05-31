package es.gaspardev.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.gaspardev.core.domain.entities.comunication.Message
import es.gaspardev.enums.MessageStatus
import es.gaspardev.states.LoggedTrainer
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun MessageBubble(
    message: Message,
    currentUserId: String = LoggedTrainer.state.trainer!!.user.id.toString()
) {
    val isCurrentUser = message.userName == currentUserId

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isCurrentUser) {
            Spacer(modifier = Modifier.width(48.dp))
        }

        Column(
            modifier = Modifier.widthIn(max = 280.dp),
            horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = if (isCurrentUser) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isCurrentUser) 16.dp else 4.dp,
                            bottomEnd = if (isCurrentUser) 4.dp else 16.dp
                        )
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = message.content,
                    color = if (isCurrentUser) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
            ) {
                Text(
                    text = formatTime(message.sendAt),
                    fontSize = 11.sp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                )

                if (isCurrentUser) {
                    Spacer(modifier = Modifier.width(4.dp))
                    MessageStatusIcon(message.messageStatus)
                }
            }
        }

        if (isCurrentUser) {
            Spacer(modifier = Modifier.width(48.dp))
        }
    }
}

@Composable
private fun MessageStatusIcon(status: MessageStatus) {
    val (text, color) = when (status) {
        MessageStatus.SENT -> "✓" to Color.Gray
        MessageStatus.DELIVERED -> "✓✓" to Color.Gray
        MessageStatus.READ -> "✓✓" to MaterialTheme.colors.primary
        else -> "✓" to Color.Gray
    }

    Text(
        text = text,
        fontSize = 10.sp,
        color = color,
        fontWeight = FontWeight.Bold
    )
}

@Suppress("DefaultLocale")
private fun formatTime(instant: Instant): String {
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return String.format("%02d:%02d", localDateTime.hour, localDateTime.minute)
}
