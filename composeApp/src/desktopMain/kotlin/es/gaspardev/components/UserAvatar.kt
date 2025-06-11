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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.gaspardev.core.domain.entities.users.User

enum class LayoutDirection {
    Horizontal, Vertical
}

@Composable
fun UserAvatar(
    user: User,
    layoutDirection: LayoutDirection = LayoutDirection.Horizontal,
    subtitleContent: (@Composable () -> Unit)? = null,
    extraSubtitleContent: (@Composable () -> Unit)? = null,
    rightContent: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    // Contenedor principal que puede ser Row o Column
    when (layoutDirection) {
        LayoutDirection.Horizontal -> {
            Row(
                modifier = modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MainContent(
                    user = user,
                    subtitleContent = subtitleContent,
                    extraSubtitleContent = extraSubtitleContent,
                    isHorizontal = true
                )

                if (rightContent != null) {
                    rightContent.invoke()
                }
            }
        }

        LayoutDirection.Vertical -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MainContent(
                    user = user,
                    subtitleContent = subtitleContent,
                    extraSubtitleContent = extraSubtitleContent,
                    isHorizontal = false
                )

                if (rightContent != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    rightContent.invoke()
                }
            }
        }
    }
}

@Composable
private fun MainContent(
    user: User,
    subtitleContent: (@Composable () -> Unit)?,
    extraSubtitleContent: (@Composable () -> Unit)?,
    isHorizontal: Boolean
) {
    if (isHorizontal) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarContent(user)
            Spacer(modifier = Modifier.width(12.dp))
            TextContent(user, subtitleContent, extraSubtitleContent)
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AvatarContent(user)
            Spacer(modifier = Modifier.height(12.dp))
            TextContent(user, subtitleContent, extraSubtitleContent)
        }
    }
}

@Composable
private fun AvatarContent(user: User) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colors.primary.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = user.getInitials(),
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.primary
        )
    }
}

@Composable
private fun TextContent(
    user: User,
    subtitleContent: (@Composable () -> Unit)?,
    extraSubtitleContent: (@Composable () -> Unit)?,
    isHorizontal: Boolean = true
) {
    Column(
        horizontalAlignment = if (isHorizontal) Alignment.Start else Alignment.CenterHorizontally
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = user.fullname,
                style = MaterialTheme.typography.h3
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

