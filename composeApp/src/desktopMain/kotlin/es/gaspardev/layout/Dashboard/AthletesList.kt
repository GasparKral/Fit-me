package es.gaspardev.layout.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.gaspardev.components.DropdownMenuButton
import es.gaspardev.components.LastActiveText
import es.gaspardev.components.UserAvatar
import es.gaspardev.core.Action
import es.gaspardev.core.LocalRouter
import es.gaspardev.core.RouterState
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.entities.workouts.WorkoutPlan
import es.gaspardev.core.domain.usecases.update.workout.UpdateWorkout
import es.gaspardev.enums.OpeningMode
import es.gaspardev.enums.StatusState
import es.gaspardev.helpers.editWorkout
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.DialogState
import es.gaspardev.layout.dialogs.DietDialog
import es.gaspardev.pages.Routes
import es.gaspardev.states.ConversationsState
import es.gaspardev.states.LoggedTrainer
import fit_me.composeapp.generated.resources.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import org.jetbrains.compose.resources.stringResource

@Composable
fun AthletesList(scope: CoroutineScope) {
    val router = LocalRouter.current

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp, 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {


        LoggedTrainer.state.athletes!!.take(5).forEach { athlete ->
            AthleteListItem(athlete, router, scope)
        }


        OutlinedButton(
            onClick = { router.navigateTo(Routes.Athletes) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colors.primary
            )
        ) {
            Text(stringResource(Res.string.view_all_athletes), color = MaterialTheme.colors.primary)
        }
    }
}

@Composable
fun AthleteListItem(athlete: Athlete, router: RouterState, scope: CoroutineScope) {
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
            subtitleContent = {
                if (athlete.needAssistant) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colors.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(Res.string.needs_plan),
                            color = MaterialTheme.colors.onPrimary,
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            },
            extraSubtitleContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LastActiveText(
                        lastActive = athlete.user.status.lastTimeActive,
                        timeZone = TimeZone.UTC
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (athlete.user.status.state == StatusState.ACTIVE) MaterialTheme.colors.primary.copy(
                                    alpha = 0.1f
                                )
                                else MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (athlete.user.status.state == StatusState.ACTIVE) stringResource(Res.string.active) else stringResource(
                                Res.string.inactive
                            ),
                            style = MaterialTheme.typography.overline,
                            color = if (athlete.user.status.state == StatusState.ACTIVE) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface.copy(
                                alpha = 0.6f
                            ),
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            },
            rightContent = {
                // Right side - Progress and actions
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${athlete.getOverallProgression()}%",
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Medium
                    )

                    Icon(
                        imageVector = FitMeIcons.Weight,
                        contentDescription = "Workout",
                        tint = if (athlete.workout == null) MaterialTheme.colors.onSurface.copy(.6f) else MaterialTheme.colors.primary
                    )

                    Icon(
                        imageVector = FitMeIcons.Nutrition,
                        contentDescription = "Nutrition",
                        tint = if (athlete.diet == null) MaterialTheme.colors.onSurface.copy(.6f) else MaterialTheme.colors.primary
                    )

                    IconButton(
                        onClick = {
                            router.executeAction(
                                Action.SimpleAction.create {
                                    ConversationsState.selectConversation(athlete.user)
                                    router.navigateTo(Routes.Messages)
                                }
                            )
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = FitMeIcons.Messages,
                            contentDescription = "Message",
                            tint = MaterialTheme.colors.primary
                        )
                    }

                    Box {
                        DropdownMenuButton(
                            items = listOf(
                                {
                                    Text(
                                        stringResource(Res.string.view_profile),
                                        style = MaterialTheme.typography.subtitle2
                                    )
                                },
                                {
                                    Text(
                                        stringResource(Res.string.edit_workout),
                                        style = MaterialTheme.typography.subtitle2,
                                        color = if (athlete.workout == null) MaterialTheme.colors.onSurface.copy(alpha = 0.6f) else MaterialTheme.colors.onSurface
                                    )
                                },
                                {
                                    Text(
                                        stringResource(Res.string.edit_diet),
                                        style = MaterialTheme.typography.subtitle2,
                                        color = if (athlete.workout == null) MaterialTheme.colors.onSurface.copy(alpha = 0.6f) else MaterialTheme.colors.onSurface
                                    )
                                }
                            ),
                            onItemSelected = { index, funtion ->
                                when (index) {
                                    0 -> {
                                        router.navigateTo(Routes.AthleteInfo.load(athlete))
                                        showDropdown = false
                                        funtion(false)
                                    }

                                    1 -> {
                                        if (athlete.workout != null) {
                                            router.executeAction(Action.SimpleAction.create {
                                                router.navigateTo(Routes.AthleteInfo.load(athlete))
                                                editWorkout(athlete.workout!!) {
                                                    scope.launch {
                                                        UpdateWorkout().run(WorkoutPlan.fromWorkout(it)).fold(
                                                            {}
                                                        )
                                                    }
                                                }
                                            })
                                            showDropdown = false
                                            funtion(false)
                                        }
                                        funtion(true)
                                    }

                                    2 -> {
                                        if (athlete.diet != null) {
                                            router.executeAction(Action.SimpleAction.create {
                                                router.navigateTo(Routes.AthleteInfo.load(athlete))
                                                DialogState.openWith {
                                                    DietDialog(
                                                        athlete.diet!!,
                                                        mode = OpeningMode.EDIT
                                                    ) { }
                                                }
                                            })
                                            showDropdown = false
                                            funtion(false)
                                        }
                                        funtion(true)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        )
    }
}

