package es.gaspardev.layout.Dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.gaspardev.icons.FitMeIcons

@Composable
fun AthletesList() {
    val athletes = listOf(
        Athlete(
            id = "1",
            name = "Carlos Rodriguez",
            image = null,
            initials = "CR",
            status = "active",
            lastActive = "2 hours ago",
            progress = 85,
            needsAttention = false
        ),
        Athlete(
            id = "2",
            name = "Maria Garcia",
            image = null,
            initials = "MG",
            status = "active",
            lastActive = "Just now",
            progress = 92,
            needsAttention = false
        ),
        Athlete(
            id = "3",
            name = "Juan Lopez",
            image = null,
            initials = "JL",
            status = "inactive",
            lastActive = "3 days ago",
            progress = 45,
            needsAttention = true
        ),
        Athlete(
            id = "4",
            name = "Ana Martinez",
            image = null,
            initials = "AM",
            status = "active",
            lastActive = "5 hours ago",
            progress = 78,
            needsAttention = false
        )
    )

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp, 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        athletes.forEach { athlete ->
            AthleteListItem(athlete = athlete)
        }

        OutlinedButton(
            onClick = { /* View all athletes */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colors.primary
            )
        ) {
            Text("View All Athletes")
        }
    }
}

@Composable
fun AthleteListItem(athlete: Athlete) {
    var showDropdown by remember { mutableStateOf(false) }

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
                /* athlete.image?.let { image ->
                     Image(
                         painter = imageResource(3),
                         contentDescription = athlete.name,
                         modifier = Modifier.fillMaxSize()
                     )
                 } ?: run {
                     Text(
                         text = athlete.initials,
                         style = MaterialTheme.typography.subtitle1,
                         color = MaterialTheme.colors.primary
                     )
                 }*/
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = athlete.name,
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Medium
                    )

                    if (athlete.needsAttention) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colors.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Needs Plan",
                                color = MaterialTheme.colors.onPrimary,
                                style = MaterialTheme.typography.caption,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Last active: ${athlete.lastActive}",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (athlete.status == "active") MaterialTheme.colors.primary.copy(alpha = 0.1f)
                                else MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = athlete.status,
                            style = MaterialTheme.typography.overline,
                            color = if (athlete.status == "active") MaterialTheme.colors.primary
                            else MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        // Right side - Progress and actions
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "${athlete.progress}%",
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Medium
            )

            IconButton(
                onClick = { /* Workout */ },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = FitMeIcons.Weight,
                    contentDescription = "Workout",
                    tint = MaterialTheme.colors.primary
                )
            }

            IconButton(
                onClick = { /* Nutrition */ },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = FitMeIcons.Nutrition,
                    contentDescription = "Nutrition",
                    tint = MaterialTheme.colors.primary
                )
            }

            IconButton(
                onClick = { /* Message */ },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = FitMeIcons.Messages,
                    contentDescription = "Message",
                    tint = MaterialTheme.colors.primary
                )
            }

            Box {
                IconButton(
                    onClick = { showDropdown = true },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "More options",
                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                }

                DropdownMenu(
                    expanded = showDropdown,
                    onDismissRequest = { showDropdown = false }
                ) {
                    DropdownMenuItem(onClick = {
                        /* View Profile */
                        showDropdown = false
                    }) {
                        Text("View Profile")
                    }
                    Divider()
                    DropdownMenuItem(onClick = {
                        /* Edit Workout Plan */
                        showDropdown = false
                    }) {
                        Text("Edit Workout Plan")
                    }
                    DropdownMenuItem(onClick = {
                        /* Edit Nutrition Plan */
                        showDropdown = false
                    }) {
                        Text("Edit Nutrition Plan")
                    }
                    DropdownMenuItem(onClick = {
                        /* Send Message */
                        showDropdown = false
                    }) {
                        Text("Send Message")
                    }
                }
            }
        }
    }
}


data class Athlete(
    val id: String,
    val name: String,
    val image: String?,
    val initials: String,
    val status: String,
    val lastActive: String,
    val progress: Int,
    val needsAttention: Boolean
)