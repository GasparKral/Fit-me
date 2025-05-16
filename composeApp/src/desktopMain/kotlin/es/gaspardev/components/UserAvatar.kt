package es.gaspardev.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.gaspardev.core.domain.entities.User
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource


@Composable
fun UserAvatar(
    user: User,
    subtitleContent: (@Composable () -> Unit)? = null,
    extraSubtitleContent: (@Composable () -> Unit)? = null,
    rightContent: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left side - Avatar and info
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                user.userImage?.let { image ->
                    @Suppress("DEPRECATION")
                    KamelImage(
                        resource = asyncPainterResource(image.src),
                        contentDescription = "Avatar of ${user.name}",
                        modifier = Modifier.size(200.dp),
                        contentScale = ContentScale.Fit,
                        onLoading = { it ->
                            Text(
                                text = user.getInitials(),
                                style = MaterialTheme.typography.subtitle1,
                                color = MaterialTheme.colors.primary
                            )
                        },
                        onFailure = { it ->
                            Text(
                                text = user.getInitials(),
                                style = MaterialTheme.typography.subtitle1,
                                color = MaterialTheme.colors.primary
                            )
                        }
                    )
                } ?: run {
                    Text(
                        text = user.getInitials(),
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.primary
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Medium
                    )
                    if (subtitleContent != null) {
                        subtitleContent.invoke()
                    }
                }
                if (extraSubtitleContent != null) {
                    extraSubtitleContent.invoke()
                }
            }
        }

        if (rightContent != null) {
            rightContent.invoke()
        }
    }
}