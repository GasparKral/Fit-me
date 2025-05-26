package es.gaspardev

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import es.gaspardev.core.LocalRouter
import es.gaspardev.core.Router
import es.gaspardev.layout.sideBar.SideBarMenu
import es.gaspardev.pages.Routes
import fit_me.composeapp.generated.resources.Outfit_Variable
import fit_me.composeapp.generated.resources.Res
import fit_me.composeapp.generated.resources.`RobotoSerif_VariableFont_GRAD,opsz,wdth,wght`
import org.jetbrains.compose.resources.Font
import java.awt.Toolkit


@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val RobotoSerif = FontFamily(Font(Res.font.`RobotoSerif_VariableFont_GRAD,opsz,wdth,wght`))
    val OutFit = FontFamily(Font(Res.font.Outfit_Variable))

    // Definición de la paleta de colores
    val CustomColorPalette = lightColors(
        primary = Color(0xFFFF4000),            // Naranja energía (se mantiene)
        primaryVariant = Color(0xFFE03A00),     // Variante más oscura del naranja
        secondary = Color(0xFF005DFF),          // Azul eléctrico (enfocado, atlético)
        secondaryVariant = Color(0xFF003EB3),   // Azul más profundo (para énfasis)

        background = Color(0xFFF6EEE5),
        surface = Color(0xFFFFFAFA),
        error = Color(0xFFD32F2F),               // Rojo deportivo y técnico, con contraste
        onPrimary = Color.White,                 // Texto sobre naranja
        onSecondary = Color.White,               // Texto sobre azul
        onBackground = Color(0xFF121212),        // Texto negro-azulado profundo
        onSurface = Color(0xFF1E1E1E),           // Texto casi negro sobre blanco
        onError = Color.White                    // Texto blanco sobre error
    )


    val CustomShapes = Shapes(
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(12.dp),   // Usar en botones y tarjetas
        large = RoundedCornerShape(24.dp)     // Usar en diálogos o elementos destacados
    )


    val CustomTypography = androidx.compose.material.Typography(
        h1 = TextStyle(
            fontFamily = RobotoSerif,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 64.sp,
            letterSpacing = (-0.5).sp
        ),
        h2 = TextStyle(
            fontFamily = RobotoSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            letterSpacing = (-0.25).sp
        ),
        h3 = TextStyle(
            fontFamily = RobotoSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            letterSpacing = 0.sp
        ),
        h4 = TextStyle(fontFamily = RobotoSerif),
        h5 = TextStyle(fontFamily = RobotoSerif),
        h6 = TextStyle(fontFamily = RobotoSerif),
        subtitle1 = TextStyle(
            fontFamily = RobotoSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            letterSpacing = 0.15.sp
        ),
        subtitle2 = TextStyle(
            fontFamily = RobotoSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            letterSpacing = 0.1.sp
        ),
        body1 = TextStyle(
            fontFamily = OutFit,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            letterSpacing = 0.5.sp
        ),
        body2 = TextStyle(
            fontFamily = OutFit,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            letterSpacing = 0.25.sp
        ),
        button = TextStyle(
            fontFamily = OutFit,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            letterSpacing = 1.25.sp
        ),
        caption = TextStyle(
            fontFamily = OutFit,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            letterSpacing = 0.4.sp
        ),
        overline = TextStyle(
            fontFamily = RobotoSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 10.sp,
            letterSpacing = 1.5.sp
        )
    )

    MaterialTheme(
        colors = CustomColorPalette,
        typography = CustomTypography,
        shapes = CustomShapes,
        content = content
    )
}


val screenSize = Toolkit.getDefaultToolkit().screenSize
val SCREEN_WIDTH = screenSize.width.dp
val SCREEN_HEIGHT = screenSize.height.dp
val windowsState = WindowState(placement = WindowPlacement.Maximized)


fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        state = windowsState,
        title = "Fit-me",
    ) {
        AppTheme {
            val scope = rememberCoroutineScope()

            Router(Routes.Login, executionScope = scope) { content ->
                val controller = LocalRouter.current
                Box(
                    modifier = Modifier
                        .fillMaxSize()

                ) {
                    val isVisible = controller.currentRoute != Routes.Login && controller.currentRoute != Routes.Regist

                    if (isVisible) {
                        SideBarMenu(controller)
                    }
                    Box(
                        modifier = Modifier.padding(start = if (isVisible) 255.dp else 0.dp)
                            .background(MaterialTheme.colors.background)
                    ) {
                        content()
                    }
                }
            }
        }
    }
}

