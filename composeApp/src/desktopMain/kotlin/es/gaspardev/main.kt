package es.gaspardev

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import es.gaspardev.core.Routing.Route
import es.gaspardev.core.Routing.Router
import es.gaspardev.core.Routing.RouterController
import es.gaspardev.layout.SideBarMenu
import java.awt.Toolkit

// Definición de la paleta de colores
private val LightColorPalette = lightColors(
    primary = Color(0xFFFF4000),
    primaryVariant = Color(0xFFFF4000),
    secondary = Color(0xFF00FF40),
    background = Color(0xFFFAFAFA),
    surface = Color(0xFFFAFAFA),
    error = Color(0xFFEF000C),
    onPrimary = Color(0xFFFAFAFA),
    onSecondary = Color(0xFF1C1C1C),
    onBackground = Color(0xFF1C1C1C),
    onSurface = Color(0xFF1C1C1C),
    onError = Color(0xFFFAFAFA)
)


@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = LightColorPalette,
        shapes = Shapes(),
        content = content
    )
}


val screenSize = Toolkit.getDefaultToolkit().screenSize
val SCREEN_WIDTH = screenSize.width.dp
val SCREEN_HEIGHT = screenSize.height.dp
val windowsState = WindowState(placement = WindowPlacement.Maximized)


fun main() = application {

    var controller: RouterController = Router {}

    Window(
        onCloseRequest = ::exitApplication,
        state = windowsState,
        title = "Fit-me",
    ) {
        AppTheme {
            controller = Router { content ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = if (controller.currentRoute.value != Route.Login && controller.currentRoute.value != Route.Regist) 255.dp else 0.dp
                        )
                ) {
                    content()
                }
            }
            if (controller.currentRoute.value != Route.Login && controller.currentRoute.value != Route.Regist) {
                SideBarMenu(controller)
            }
        }
    }
}

