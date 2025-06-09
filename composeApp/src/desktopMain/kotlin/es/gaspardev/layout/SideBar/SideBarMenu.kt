package es.gaspardev.layout.sideBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import es.gaspardev.core.RouterState
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.pages.Routes
import es.gaspardev.states.LoggedTrainer
import fit_me.composeapp.generated.resources.Res
import fit_me.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SideBarMenu(
    controller: RouterState,
    isCollapsed: Boolean,
    onToggleCollapse: () -> Unit
) {
    val PADDING = 16
    val sidebarWidth = if (isCollapsed) 80.dp else 255.dp

    FlowColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .width(sidebarWidth)
            .fillMaxHeight()
            .background(MaterialTheme.colors.onPrimary)
            .padding(PADDING.dp)
            .drawBehind {
                val borderWidth = 1.dp.toPx()
                drawRect(
                    color = Color(0xFFD9D9D9),
                    topLeft = androidx.compose.ui.geometry.Offset(
                        size.width - borderWidth + PADDING,
                        0f - PADDING
                    ),
                    size = androidx.compose.ui.geometry.Size(
                        borderWidth,
                        size.height + PADDING * 2
                    )
                )
            }
    ) {
        // Header con botón de colapsar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = if (isCollapsed) 0.dp else PADDING.dp,
                    end = if (isCollapsed) 0.dp else PADDING.dp,
                    bottom = PADDING.dp
                )
                .drawBehind {
                    val borderWidth = 1.dp.toPx()
                    drawRect(
                        color = Color(0xFFD9D9D9),
                        topLeft = androidx.compose.ui.geometry.Offset(
                            0f - PADDING * 2,
                            size.height - borderWidth + PADDING / 2
                        ),
                        size = androidx.compose.ui.geometry.Size(
                            size.width + PADDING * 4,
                            borderWidth
                        )
                    )
                }
        ) {
            if (isCollapsed) {
                // Solo mostrar el botón de menú cuando está colapsado
                IconButton(
                    onClick = onToggleCollapse,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Expandir menú",
                        tint = MaterialTheme.colors.primary
                    )
                }
            } else {
                // Mostrar título y botón de colapsar cuando está expandido
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        color = MaterialTheme.colors.primary,
                        text = "Fit-Me",
                        style = MaterialTheme.typography.h2
                    )
                    IconButton(onClick = onToggleCollapse) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Colapsar menú",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
            }
        }

        // Elementos del menú
        SideBarMenuItem(
            text = stringResource(Res.string.home),
            path = Routes.Dashboard,
            controller = controller,
            icon = Icons.Default.Home,
            isCollapsed = isCollapsed
        )
        SideBarMenuItem(
            text = stringResource(Res.string.athletes),
            path = Routes.Athletes,
            controller = controller,
            icon = FitMeIcons.Athlets,
            isCollapsed = isCollapsed
        )
        SideBarMenuItem(
            text = stringResource(Res.string.workouts),
            path = Routes.Workouts,
            controller = controller,
            icon = FitMeIcons.Weight,
            isCollapsed = isCollapsed
        )
        SideBarMenuItem(
            text = stringResource(Res.string.nutrition),
            path = Routes.Nutrition,
            controller = controller,
            icon = FitMeIcons.Nutrition,
            isCollapsed = isCollapsed
        )
        SideBarMenuItem(
            text = stringResource(Res.string.calendar),
            path = Routes.Calendar,
            controller = controller,
            icon = FitMeIcons.Calendar,
            isCollapsed = isCollapsed
        )
        SideBarMenuItem(
            text = stringResource(Res.string.statistics),
            path = Routes.Statistics,
            controller = controller,
            icon = FitMeIcons.ChartBar,
            isCollapsed = isCollapsed
        )
        SideBarMenuItem(
            text = stringResource(Res.string.messages),
            path = Routes.Messages,
            controller = controller,
            icon = FitMeIcons.Messages,
            isCollapsed = isCollapsed
        )
    }

    if (!isCollapsed) {
        // Perfil de usuario en la parte inferior
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            UserProfileDropdown(
                controller = controller,
                onLogout = {
                    LoggedTrainer.logout()
                    controller.navigateTo(Routes.Login)
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}