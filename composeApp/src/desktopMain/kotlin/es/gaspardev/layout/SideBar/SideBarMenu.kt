package es.gaspardev.layout.sideBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
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
fun SideBarMenu(controller: RouterState) {

    val PADDING = 16

    FlowColumn(
        modifier = Modifier
            .width(255.dp)
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
                    ), // Posición del borde derecho
                    size = androidx.compose.ui.geometry.Size(
                        borderWidth,
                        size.height + PADDING * 2
                    ) // Tamaño del borde (ancho y alto)
                )
            }
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = PADDING.dp,
                    end = PADDING.dp,
                    bottom = PADDING.dp
                )
                .drawBehind {
                    val borderWidth = 1.dp.toPx() // Ancho del borde en píxeles
                    drawRect(
                        color = Color(0xFFD9D9D9), // Color del borde
                        topLeft = androidx.compose.ui.geometry.Offset(
                            0f - PADDING * 2, // El borde comienza desde el lado izquierdo
                            size.height - borderWidth + PADDING / 2 // Posición en la parte inferior
                        ),
                        size = androidx.compose.ui.geometry.Size(
                            size.width + PADDING * 4, // El ancho del borde es igual al ancho del Box
                            borderWidth // El alto del borde es igual al grosor del borde
                        )
                    )
                }
        ) {
            Text(
                color = MaterialTheme.colors.primary,
                text = "Fit-Me",
                style = MaterialTheme.typography.h2
            )
        }

        SideBarMenuItem(stringResource(Res.string.home), Routes.Dashboard, controller, Icons.Default.Home)
        SideBarMenuItem(
            stringResource(Res.string.athletes),
            Routes.Athletes,
            controller,
            FitMeIcons.Athlets
        )
        SideBarMenuItem(
            stringResource(Res.string.workouts),
            Routes.Workouts,
            controller,
            FitMeIcons.Weight
        )
        SideBarMenuItem(
            stringResource(Res.string.nutrition),
            Routes.Nutrition,
            controller,
            FitMeIcons.Nutrition
        )
        SideBarMenuItem(
            stringResource(Res.string.calendar),
            Routes.Calendar,
            controller,
            FitMeIcons.Calendar
        )
        SideBarMenuItem(
            stringResource(Res.string.statistics),
            Routes.Statistics,
            controller,
            FitMeIcons.ChartBar
        )
        SideBarMenuItem(
            stringResource(Res.string.messages),
            Routes.Messages,
            controller,
            FitMeIcons.Messages
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        UserProfileDropdown(
            controller, {
                LoggedTrainer.logout()
                controller.navigateTo(Routes.Login)
            },
            Modifier.align(Alignment.BottomCenter)
        )
    }


}
