package es.gaspardev.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.gaspardev.core.domain.entities.comunication.Message
import es.gaspardev.enums.MessageStatus
import es.gaspardev.enums.MessageType
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun MessageBubble(
    message: Message,
    currentUserId: Int,
    showStatus: Boolean = true,
    onMessageRead: ((String) -> Unit)? = null,
    onRetryMessage: ((String) -> Unit)? = null
) {
    val isCurrentUser = message.senderId == currentUserId

    // Marcar como leído si es necesario
    LaunchedEffect(message.id) {
        if (!isCurrentUser && message.messageStatus == MessageStatus.DELIVERED && onMessageRead != null) {
            onMessageRead(message.id)
        }
    }

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
            // Bubble principal
            Box(
                modifier = Modifier
                    .background(
                        color = when {
                            message.messageStatus == MessageStatus.FAILED -> Color.Red.copy(alpha = 0.7f)
                            isCurrentUser -> MaterialTheme.colors.primary
                            else -> MaterialTheme.colors.surface
                        },
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isCurrentUser) 16.dp else 4.dp,
                            bottomEnd = if (isCurrentUser) 4.dp else 16.dp
                        )
                    )
                    .padding(12.dp)
            ) {
                Column {
                    // Contenido del mensaje
                    when (message.messageType) {
                        MessageType.TEXT -> {
                            Text(
                                text = message.content,
                                color = if (isCurrentUser || message.messageStatus == MessageStatus.FAILED)
                                    MaterialTheme.colors.onPrimary
                                else
                                    MaterialTheme.colors.onSurface,
                                fontSize = 14.sp
                            )
                        }

                        MessageType.IMAGE -> {
                            // TODO: Implementar vista de imagen
                            Text(
                                text = "📷 Image",
                                color = if (isCurrentUser) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface,
                                fontSize = 14.sp
                            )
                        }

                        MessageType.FILE -> {
                            // TODO: Implementar vista de archivo
                            Text(
                                text = "📎 File: ${message.content}",
                                color = if (isCurrentUser) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface,
                                fontSize = 14.sp
                            )
                        }

                        MessageType.SYSTEM -> {
                            Text(
                                text = message.content,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                                fontSize = 12.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        }

                        else -> {
                            Text(
                                text = message.content,
                                color = if (isCurrentUser) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface,
                                fontSize = 14.sp
                            )
                        }
                    }

                    // Botón de reintentar para mensajes fallidos
                    if (message.messageStatus == MessageStatus.FAILED && onRetryMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(
                                onClick = { onRetryMessage(message.id) },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = MaterialTheme.colors.onPrimary
                                )
                            ) {
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = "Retry",
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Retry", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Información del mensaje
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
            ) {
                Text(
                    text = formatTime(message.sentAt),
                    fontSize = 11.sp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                )

                if (isCurrentUser && showStatus) {
                    Spacer(modifier = Modifier.width(4.dp))
                    MessageStatusIndicator(message.messageStatus)
                }
            }

            // Mostrar estado de entrega/lectura si es relevante
            if (isCurrentUser && showStatus && (message.deliveredAt != null || message.readAt != null)) {
                Text(
                    text = when {
                        message.readAt != null -> "Read ${formatTime(message.readAt!!)}"
                        message.deliveredAt != null -> "Delivered ${formatTime(message.deliveredAt!!)}"
                        else -> ""
                    },
                    fontSize = 10.sp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                )
            }
        }

        if (isCurrentUser) {
            Spacer(modifier = Modifier.width(48.dp))
        }
    }
}

@Composable
private fun MessageStatusIndicator(status: MessageStatus) {
    val (icon, color) = when (status) {
        MessageStatus.SENDING -> "⏳" to Color.Gray
        MessageStatus.SENT -> "✓" to Color.Gray
        MessageStatus.DELIVERED -> "✓✓" to Color.Gray
        MessageStatus.READ -> "✓✓" to MaterialTheme.colors.primary
        MessageStatus.FAILED -> {
            Icon(
                Icons.Default.Warning,
                contentDescription = "Failed",
                modifier = Modifier.size(12.dp),
                tint = Color.Red
            )
            return
        }

        MessageStatus.ALL -> return
    }

    Text(
        text = icon,
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