package es.gaspardev.layout.Dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import kotlin.reflect.KFunction

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
                    text = "Quick Actions",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Common tasks you might want to perform",
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
                                contentDescription = action.label,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colors.onPrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = action.label,
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

// Data class for quick actions
data class QuickAction(
    val route: Route?,
    val icon: ImageVector,
    val label: String,
    val action: KFunction<Unit>
)

// Quick actions list
val quickActions = listOf(
    QuickAction(
        route = Routes.Athletes,
        icon = FitMeIcons.Athlets,
        label = "Add Athlete",
        action = ::test
    ),
    QuickAction(
        route = Routes.Workouts,
        icon = FitMeIcons.Weight,
        label = "Create Workout",
        action = es.gaspardev.layout.DialogState::open
    ),
    QuickAction(
        route = Routes.Nutrition,
        icon = FitMeIcons.Nutrition,
        label = "Create Diet",
        action = ::test
    ),
    QuickAction(
        route = Routes.Calendar,
        icon = FitMeIcons.Calendar,
        label = "Schedule Session",
        action = ::test
    ),
    QuickAction(
        route = Routes.Messages,
        icon = FitMeIcons.Messages,
        label = "Send Message",
        action = ::test
    ),
    QuickAction(
        route = null,
        icon = Icons.Default.Add,
        label = "More Actions",
        action = ::test
    )
)