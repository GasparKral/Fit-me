package es.gaspardev.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.gaspardev.core.Routing.Route
import es.gaspardev.core.Routing.RouterController
import fit_me.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun SideBarMenu(controller: RouterController) {

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

        SideBarMenuItem("Home", Route.Dashboard, controller) { Icon(painterResource(Res.drawable.Home), "Home Icon") }
        SideBarMenuItem(
            "Athletes",
            Route.Athletes,
            controller
        ) { Icon(painterResource(Res.drawable.Athlets), "Athletes Icon") }
        SideBarMenuItem(
            "Workouts",
            Route.Workouts,
            controller
        ) { Icon(painterResource(Res.drawable.Weights), "Workouts Icon") }
        SideBarMenuItem(
            "Nutrition",
            Route.Nutrition, controller
        ) { Icon(painterResource(Res.drawable.Nutrition), "Nutrition Icon") }
        SideBarMenuItem(
            "Calendar",
            Route.Calendar,
            controller
        ) { Icon(painterResource(Res.drawable.Calendar), "Calendar Icon") }
        SideBarMenuItem(
            "Statistics",
            Route.Statistics, controller
        ) { Icon(painterResource(Res.drawable.Stadistics), "Staditics Icon") }
        SideBarMenuItem(
            "Messages",
            Route.Messages,
            controller
        ) { Icon(painterResource(Res.drawable.Messages), "Message Icon") }
        SideBarMenuItem(
            "Settings",
            Route.Settings,
            controller
        ) { Icon(painterResource(Res.drawable.Settings), "Settings Icon") }
    }

}
