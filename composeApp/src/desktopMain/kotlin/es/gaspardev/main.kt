package es.gaspardev

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import es.gaspardev.core.debug.FilePrintter
import es.gaspardev.utils.Logger

fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        title = "Fit-me",
    ) {
        App()
    }
}