package es.gaspardev.layout.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dokar.sonner.ToastType
import com.dokar.sonner.ToasterState
import es.gaspardev.core.Action
import es.gaspardev.core.RouterState
import es.gaspardev.core.domain.entities.workouts.WorkoutPlan
import es.gaspardev.core.domain.usecases.create.CreateNewWorkout
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.DialogState
import es.gaspardev.layout.dialogs.DietDialog
import es.gaspardev.layout.dialogs.WorkoutDialog
import es.gaspardev.pages.Routes
import es.gaspardev.pages.agregateNewSportman
import es.gaspardev.states.LoggedTrainer
import fit_me.composeapp.generated.resources.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Immutable
data class QuickAction(
    val icon: ImageVector,
    val labelRes: StringResource,
    val actionType: Action
)

@Composable
fun QuickActions(controller: RouterState, toaster: ToasterState) {
    val scope = rememberCoroutineScope()
    val quickActions = listOf(
        QuickAction(
            icon = FitMeIcons.Athlets,
            labelRes = Res.string.add_athlete_action,
            actionType = Action.SuspendAction.create {
                controller.navigateTo(Routes.Athletes)
                agregateNewSportman(toaster)
            }
        ),
        QuickAction(
            icon = FitMeIcons.Weight,
            labelRes = Res.string.create_workout,
            actionType = Action.SimpleAction.create {
                controller.navigateTo(Routes.Workouts)
                DialogState.openWith {
                    WorkoutDialog { workout ->
                        scope.launch {
                            CreateNewWorkout().run(Pair(workout, LoggedTrainer.state.trainer!!)).fold(
                                {
                                    toaster.show(
                                        "La rutina se ha creado correctamente",
                                        type = ToastType.Success
                                    )
                                },
                                { err -> // Error
                                    toaster.show(
                                        if (err.message != null) "Error al crear la rutina" else "Error al crear la rutina: ${err.message!!}",
                                        type = ToastType.Error
                                    )
                                }
                            )
                        }
                    }
                }
            }
        ),
        QuickAction(
            icon = FitMeIcons.Nutrition,
            labelRes = Res.string.create_diet,
            actionType = Action.SuspendAction.create {
                controller.navigateTo(Routes.Nutrition)
                DialogState.openWith { DietDialog { } }
            }
        ),
        QuickAction(
            icon = FitMeIcons.Calendar,
            labelRes = Res.string.schedule_session,
            actionType = Action.SuspendAction.create { }
        ),
        QuickAction(
            icon = FitMeIcons.Messages,
            labelRes = Res.string.send_message,
            actionType = Action.Navigation(Routes.Messages)
        )
    )

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Header
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(Res.string.quick_actions_title),
                    style = MaterialTheme.typography.h2,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(Res.string.quick_actions_subtitle),
                    style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
                )
            }

            // Action Buttons
            LazyVerticalGrid(
                columns = GridCells.Fixed(quickActions.size),
                modifier = Modifier
                    .heightIn(max = 300.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(quickActions) { action ->
                    Button(
                        onClick = {
                            controller.executeAction(action.actionType)
                        }
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = action.icon,
                                contentDescription = "",
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colors.onPrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(action.labelRes),
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}


