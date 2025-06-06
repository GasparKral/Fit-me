package es.gaspardev

import fit_me.composeapp.generated.resources.Outfit_Variable
import fit_me.composeapp.generated.resources.Res
import fit_me.composeapp.generated.resources.RobotoSerif_VariableFont
import org.jetbrains.compose.resources.Font
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp


@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val robotoSerif = FontFamily(Font(Res.font.RobotoSerif_VariableFont))
    val outFit = FontFamily(Font(Res.font.Outfit_Variable))

    // Definición de la paleta de colores
    val customColorPalette = lightColors(
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


    val customShapes = Shapes(
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(12.dp),   // Usar en botones y tarjetas
        large = RoundedCornerShape(24.dp)     // Usar en diálogos o elementos destacados
    )


    val customTypography = androidx.compose.material.Typography(
        h1 = TextStyle(
            fontFamily = robotoSerif,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 64.sp,
            letterSpacing = (-0.5).sp
        ),
        h2 = TextStyle(
            fontFamily = robotoSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            letterSpacing = (-0.25).sp
        ),
        h3 = TextStyle(
            fontFamily = robotoSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            letterSpacing = 0.sp
        ),
        h4 = TextStyle(fontFamily = robotoSerif),
        h5 = TextStyle(fontFamily = robotoSerif),
        h6 = TextStyle(fontFamily = robotoSerif),
        subtitle1 = TextStyle(
            fontFamily = robotoSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            letterSpacing = 0.15.sp
        ),
        subtitle2 = TextStyle(
            fontFamily = robotoSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            letterSpacing = 0.1.sp
        ),
        body1 = TextStyle(
            fontFamily = outFit,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            letterSpacing = 0.5.sp
        ),
        body2 = TextStyle(
            fontFamily = outFit,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            letterSpacing = 0.25.sp
        ),
        button = TextStyle(
            fontFamily = outFit,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            letterSpacing = 1.25.sp
        ),
        caption = TextStyle(
            fontFamily = outFit,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            letterSpacing = 0.4.sp
        ),
        overline = TextStyle(
            fontFamily = robotoSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 10.sp,
            letterSpacing = 1.5.sp
        )
    )

    MaterialTheme(
        colors = customColorPalette,
        typography = customTypography,
        shapes = customShapes,
        content = content
    )
}