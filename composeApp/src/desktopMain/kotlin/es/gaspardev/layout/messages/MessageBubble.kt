package es.gaspardev.layout.messages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.gaspardev.components.UserAvatar
import es.gaspardev.core.domain.entities.comunication.Message
import es.gaspardev.core.domain.entities.users.User
import es.gaspardev.enums.MessageStatus
import es.gaspardev.enums.MessageStatus.DELIVERED
import es.gaspardev.enums.MessageStatus.READ
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun MessageBubble(message: Message, user: User) {
    val isSent = message.messageStatus == MessageStatus.SENT

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = if (isSent) Alignment.End else Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = if (isSent) Arrangement.End else Arrangement.Start
        ) {
            if (!isSent) {
                UserAvatar(user)
                Spacer(Modifier.width(8.dp))
            }
            Column(
                horizontalAlignment = if (isSent) Alignment.End else Alignment.Start
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSent) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
                    contentColor = if (isSent) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = message.content,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message.sendAt.toLocalDateTime(TimeZone.currentSystemDefault()).toString(),
                        fontSize = 12.sp,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                    if (isSent) {
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = if (message.messageStatus == READ) {
                                "Read"
                            } else if (message.messageStatus == DELIVERED) {
                                "Delivered"
                            } else "Send",
                            fontSize = 12.sp,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}
