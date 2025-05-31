package es.gaspardev

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.isAltPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import es.gaspardev.core.LocalRouter
import es.gaspardev.core.Router
import es.gaspardev.core.RouterState
import es.gaspardev.layout.FloatingDialog
import es.gaspardev.layout.sideBar.SideBarMenu
import es.gaspardev.pages.Routes
import es.gaspardev.states.LoggedTrainer
import fit_me.composeapp.generated.resources.Res
import fit_me.composeapp.generated.resources.window_title
import org.jetbrains.compose.resources.stringResource
import java.awt.Dimension
import java.awt.Toolkit

val screenSize: Dimension = Toolkit.getDefaultToolkit().screenSize
val SCREEN_HEIGHT = screenSize.height.dp
val windowsState = WindowState(placement = WindowPlacement.Maximized)


fun main() = application {

    var router: RouterState? by remember { mutableStateOf(null) }

    val actions: (KeyEvent) -> Boolean = { event ->
        when {
            LoggedTrainer.state.trainer == null || router == null -> false
            event.isAltPressed && (event.key == Key.One || event.key == Key.NumPad1) -> {
                router?.navigateTo(Routes.Dashboard)
                true
            }

            event.isAltPressed && (event.key == Key.Two || event.key == Key.NumPad2) -> {
                router?.navigateTo(Routes.Athletes)
                true
            }

            event.isAltPressed && (event.key == Key.Three || event.key == Key.NumPad3) -> {
                router?.navigateTo(Routes.Workouts)
                true
            }

            event.isAltPressed && (event.key == Key.Four || event.key == Key.NumPad4) -> {
                router?.navigateTo(Routes.Nutrition)
                true
            }

            event.isAltPressed && (event.key == Key.Five || event.key == Key.NumPad5) -> {
                router?.navigateTo(Routes.Calendar)
                true
            }

            event.isAltPressed && (event.key == Key.Six || event.key == Key.NumPad6) -> {
                router?.navigateTo(Routes.Statistics)
                true
            }

            event.isAltPressed && (event.key == Key.Seven || event.key == Key.NumPad7) -> {
                router?.navigateTo(Routes.Messages)
                true
            }

            else -> false
        }
    }

    Window(
        onCloseRequest = ::exitApplication,
        state = windowsState,
        title = stringResource(Res.string.window_title),
        onKeyEvent = actions
    ) {
        AppTheme {
            val scope = rememberCoroutineScope()
            Router(Routes.Login, executionScope = scope) { content ->
                val controller = LocalRouter.current
                LaunchedEffect(controller) {
                    router = controller
                }
                // En tu función main(), cambia esta parte:
                BoxWithConstraints(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val isVisible = controller.currentRoute != Routes.Login && controller.currentRoute != Routes.Regist

                    if (isVisible) {
                        SideBarMenu(controller)
                    }

                    Box(
                        modifier = Modifier
                            .padding(start = if (isVisible) 255.dp else 0.dp)
                            .background(MaterialTheme.colors.background)
                    ) {
                        content()
                    }

                    FloatingDialog(maxSize = Pair(maxWidth, maxHeight))
                }
            }
        }
    }
}

