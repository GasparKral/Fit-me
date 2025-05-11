package es.gaspardev.layout.SideBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.gaspardev.controllers.LoggedUser
import es.gaspardev.core.RouterState
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.SideBar.SideBar.SideBarMenuItem
import es.gaspardev.pages.Routes

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
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        }

        SideBarMenuItem("Home", Routes.Dashboard, controller, Icons.Default.Home)
        SideBarMenuItem(
            "Athletes",
            Routes.Athletes,
            controller,
            FitMeIcons.Athlets
        )
        SideBarMenuItem(
            "Workouts",
            Routes.Workouts,
            controller,
            FitMeIcons.Weight
        )
        SideBarMenuItem(
            "Nutrition",
            Routes.Nutrition,
            controller,
            FitMeIcons.Nutrition
        )
        SideBarMenuItem(
            "Calendar",
            Routes.Calendar,
            controller,
            FitMeIcons.Calendar
        )
        SideBarMenuItem(
            "Statistics",
            Routes.Statistics,
            controller,
            FitMeIcons.ChartBar
        )
        SideBarMenuItem(
            "Messages",
            Routes.Messages,
            controller,
            FitMeIcons.Messages
        )
        SideBarMenuItem(
            "Settings",
            Routes.Settings,
            controller,
            Icons.Default.Settings
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        UserProfileDropdown(
            controller, {
                LoggedUser.logOut()
                controller.navigateTo(Routes.Login)
            },
            Modifier.align(Alignment.BottomCenter)
        )
    }


}
