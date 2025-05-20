package es.gaspardev.layout.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import es.gaspardev.components.UserAvatar
import es.gaspardev.core.LocalRouter
import es.gaspardev.core.domain.entities.Sportsman
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.pages.Routes
import es.gaspardev.states.LoggedTrainer
import fit_me.composeapp.generated.resources.Res
import fit_me.composeapp.generated.resources.active
import fit_me.composeapp.generated.resources.inactive
import org.jetbrains.compose.resources.stringResource

@Composable
fun AthletesList() {
    val router = LocalRouter.current

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp, 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        if (LoggedTrainer.state.trainer != null) {
            LoggedTrainer.state.trainer!!.sportmans.take(5).forEach { athlete ->
                AthleteListItem(athlete = athlete)
            }
        }

        OutlinedButton(
            onClick = { router.navigateTo(Routes.Athletes) },
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
fun AthleteListItem(athlete: Sportsman) {
    var showDropdown by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        UserAvatar(
            athlete.user,
            {
                if (athlete.user.status.needsAttetion) {
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
            },
            {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Last active: ${athlete.user.status.lastActive}",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (athlete.user.status.status) MaterialTheme.colors.primary.copy(alpha = 0.1f)
                                else MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (athlete.user.status.status) stringResource(Res.string.active) else stringResource(
                                Res.string.inactive
                            ),
                            style = MaterialTheme.typography.overline,
                            color = if (athlete.user.status.status) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface.copy(
                                alpha = 0.6f
                            ),
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            },
            {
                // Right side - Progress and actions
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${athlete.getWorkoutProgression()}%",
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
        )
    }
}

