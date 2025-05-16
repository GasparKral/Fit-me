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
import es.gaspardev.pages.Message
import es.gaspardev.pages.sampleUser

@Composable
fun MessageBubble(message: Message) {
    val isSent = message.senderId == "trainer"

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
                UserAvatar(sampleUser[0])
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
                        text = message.timestamp,
                        fontSize = 12.sp,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                    if (isSent) {
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = if (message.status == "read") "Read" else "Delivered",
                            fontSize = 12.sp,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}
