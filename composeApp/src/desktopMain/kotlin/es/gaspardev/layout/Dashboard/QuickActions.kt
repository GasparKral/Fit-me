package es.gaspardev.layout.dashboard

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.gaspardev.core.Route
import es.gaspardev.core.RouterState
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.pages.Routes
import es.gaspardev.pages.test
import fit_me.composeapp.generated.resources.*
import fit_me.composeapp.generated.resources.Res
import fit_me.composeapp.generated.resources.add_athlete_action
import fit_me.composeapp.generated.resources.create_workout
import kotlin.reflect.KFunction
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun QuickActions(controller: RouterState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column {
            // Header
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(Res.string.quick_actions_title),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(Res.string.quick_actions_subtitle),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
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
                            if (action.route != null) controller.navigateToWithAction(
                                action.route,
                                action.action,
                                null
                            ) else action.action.call()
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
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

data class QuickAction(
    val route: Route?,
    val icon: ImageVector,
    val labelRes: StringResource,
    val action: KFunction<Unit>
)

val quickActions = listOf(
    QuickAction(
        route = Routes.Athletes,
        icon = FitMeIcons.Athlets,
        labelRes = Res.string.add_athlete_action,
        action = ::test
    ),
    QuickAction(
        route = Routes.Workouts,
        icon = FitMeIcons.Weight,
        labelRes = Res.string.create_workout,
        action = es.gaspardev.layout.DialogState::open
    ),
    QuickAction(
        route = Routes.Nutrition,
        icon = FitMeIcons.Nutrition,
        labelRes = Res.string.create_diet,
        action = ::test
    ),
    QuickAction(
        route = Routes.Calendar,
        icon = FitMeIcons.Calendar,
        labelRes = Res.string.schedule_session,
        action = ::test
    ),
    QuickAction(
        route = Routes.Messages,
        icon = FitMeIcons.Messages,
        labelRes = Res.string.send_message,
        action = ::test
    )
)