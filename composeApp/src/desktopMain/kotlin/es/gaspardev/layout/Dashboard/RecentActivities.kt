package es.gaspardev.layout.Dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.gaspardev.icons.FitMeIcons

@Composable
fun RecentActivities() {
    val activities = listOf(
        Activity(
            id = "1",
            athlete = AthleteShort(
                name = "Carlos Rodriguez",
                image = null,
                initials = "CR"
            ),
            type = "workout",
            action = "completed",
            item = "Upper Body Strength",
            time = "2 hours ago",
            status = "success",
            needsAction = false
        ),
        Activity(
            id = "2",
            athlete = AthleteShort(
                name = "Maria Garcia",
                image = null,
                initials = "MG"
            ),
            type = "nutrition",
            action = "logged",
            item = "Daily Meal Plan",
            time = "4 hours ago",
            status = "success",
            needsAction = false
        ),
        Activity(
            id = "3",
            athlete = AthleteShort(
                name = "Juan Lopez",
                image = null,
                initials = "JL"
            ),
            type = "message",
            action = "sent",
            item = "Question about routine",
            time = "Yesterday",
            status = "pending",
            needsAction = false
        ),
        Activity(
            id = "4",
            athlete = AthleteShort(
                name = "Ana Martinez",
                image = null,
                initials = "AM"
            ),
            type = "workout",
            action = "missed",
            item = "Cardio Session",
            time = "2 days ago",
            status = "warning",
            needsAction = true
        ),
        Activity(
            id = "5",
            athlete = AthleteShort(
                name = "Carlos Rodriguez",
                image = null,
                initials = "CR"
            ),
            type = "message",
            action = "sent",
            item = "Progress update",
            time = "3 days ago",
            status = "success",
            needsAction = false
        )
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        activities.forEach { activity ->
            ActivityItem(activity = activity)
        }
    }
}

@Composable
fun ActivityItem(activity: Activity) {
    val hoverInteraction = remember { MutableInteractionSource() }
    val isHovered by hoverInteraction.collectIsHoveredAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .hoverable(interactionSource = hoverInteraction)
            .background(
                if (isHovered) MaterialTheme.colors.onSurface.copy(alpha = 0.04f)
                else Color.Transparent,
                shape = MaterialTheme.shapes.small
            ),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Athlete avatar
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            activity.athlete.image?.let { image ->
                /*Image(
                    painter = rememberImagePainter(image),
                    contentDescription = activity.athlete.name,
                    modifier = Modifier.fillMaxSize()
                )*/
            } ?: run {
                Text(
                    text = activity.athlete.initials,
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.primary
                )
            }
        }

        // Activity details
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = activity.athlete.name,
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.Medium
                )

                // Type badge
                Surface(
                    shape = MaterialTheme.shapes.small,
                    border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.12f)),
                    color = Color.Transparent
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ActivityTypeIcon(type = activity.type)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = activity.type.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.overline
                        )
                    }
                }
            }

            Text(
                text = "${activity.action.replaceFirstChar { it.uppercase() }} ${activity.item}",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )

            Text(
                text = activity.time,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
        }

        // Status and action
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ActivityStatusIcon(status = activity.status)

            if (activity.needsAction) {
                Button(
                    onClick = { /* Follow up */ },
                    modifier = Modifier.height(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Text(
                        text = "Follow Up",
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }
    }
}

@Composable
fun ActivityTypeIcon(type: String) {
    val icon = when (type) {
        "workout" -> FitMeIcons.Weight
        "nutrition" -> FitMeIcons.Nutrition
        "message" -> FitMeIcons.Messages
        else -> null
    }

    icon?.let {
        Icon(
            imageVector = it,
            contentDescription = type,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun ActivityStatusIcon(status: String) {
    val (icon, color) = when (status) {
        "success" -> Pair(Icons.Default.CheckCircle, MaterialTheme.colors.primary)
        "warning" -> Pair(Icons.Default.Warning, Color(0xFFFFA000)) // Amber 500
        "pending" -> Pair(Icons.Default.Info, Color(0xFF2196F3)) // Blue 500
        else -> Pair(null, Color.Unspecified)
    }

    icon?.let {
        Icon(
            imageVector = it,
            contentDescription = status,
            modifier = Modifier.size(16.dp),
            tint = color
        )
    }
}

data class Activity(
    val id: String,
    val athlete: AthleteShort,
    val type: String,
    val action: String,
    val item: String,
    val time: String,
    val status: String,
    val needsAction: Boolean
)

data class AthleteShort(
    val name: String,
    val image: String?,
    val initials: String
)