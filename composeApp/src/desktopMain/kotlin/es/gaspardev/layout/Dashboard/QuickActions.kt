package es.gaspardev.layout.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.gaspardev.core.Action
import es.gaspardev.core.RouterState
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.DialogState
import es.gaspardev.layout.dialogs.DietCreationDialog
import es.gaspardev.layout.dialogs.WorkoutCreationDialog
import es.gaspardev.pages.Routes
import es.gaspardev.pages.agregateNewSportman
import fit_me.composeapp.generated.resources.*
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Immutable
data class QuickAction(
    val icon: ImageVector,
    val labelRes: StringResource,
    val actionType: Action
)

// Y también necesitarás actualizar el componente QuickActions:
@Composable
fun QuickActions(controller: RouterState) {

    val quickActions = listOf(
        QuickAction(
            icon = FitMeIcons.Athlets,
            labelRes = Res.string.add_athlete_action,
            actionType = Action.SuspendAction {
                controller.navigateTo(Routes.Athletes)
                agregateNewSportman()
            }
        ),
        QuickAction(
            icon = FitMeIcons.Weight,
            labelRes = Res.string.create_workout,
            actionType = Action.SimpleAction {
                controller.navigateTo(Routes.Workouts)
                DialogState.openWith { WorkoutCreationDialog { } }
            }
        ),
        QuickAction(
            icon = FitMeIcons.Nutrition,
            labelRes = Res.string.create_diet,
            actionType = Action.SuspendAction {
                controller.navigateTo(Routes.Nutrition)
                DialogState.openWith { DietCreationDialog { } }
            }
        ),
        QuickAction(
            icon = FitMeIcons.Calendar,
            labelRes = Res.string.schedule_session,
            actionType = Action.SuspendAction { agregateNewSportman() }
        ),
        QuickAction(
            icon = FitMeIcons.Messages,
            labelRes = Res.string.send_message,
            actionType = Action.SuspendAction { agregateNewSportman() }
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


