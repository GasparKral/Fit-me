package es.gaspardev

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.gaspardev.core.domain.dtos.settings.AppearanceSettingsData
import es.gaspardev.core.domain.usecases.update.settings.UpdateAppearanceSettings
import es.gaspardev.states.LoggedTrainer
import fit_me.composeapp.generated.resources.Nunito_Variable
import fit_me.composeapp.generated.resources.Res
import fit_me.composeapp.generated.resources.RobotoSerif_VariableFont
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.Font

object ThemeOption {

    var _selectedTheme by mutableStateOf("system")
    val selectedTheme get() = _selectedTheme

    fun modifyTheme(value: String) {
        _selectedTheme = value
        MainScope().launch {
            UpdateAppearanceSettings().run(
                Pair(
                    LoggedTrainer.state.trainer!!.user,
                    AppearanceSettingsData(value)
                )
            )
        }
    }

}

@Composable
fun AppTheme(
    themeOption: String = ThemeOption.selectedTheme,
    content: @Composable () -> Unit
) {
    val isDarkTheme = when (themeOption) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }
    val robotoSerif = FontFamily(Font(Res.font.RobotoSerif_VariableFont))
    val nunito = FontFamily(Font(Res.font.Nunito_Variable))

    val customColorPalette = if (isDarkTheme) {
        darkColors(
            primary = Color(0xFFFF5722),
            primaryVariant = Color(0xFFE64A19),
            secondary = Color(0xFF448AFF),
            secondaryVariant = Color(0xFF2962FF),
            background = Color(0xFF121212),
            surface = Color(0xFF1E1E1E),
            error = Color(0xFFCF6679),
            onPrimary = Color(0xFFFAFAFA),
            onSecondary = Color.White,
            onBackground = Color(0xFFE0E0E0),
            onSurface = Color(0xFFCCCCCC),
            onError = Color.Black
        )
    } else {
        lightColors(
            primary = Color(0xFFFF4000),
            primaryVariant = Color(0xFFE03A00),
            secondary = Color(0xFF005DFF),
            secondaryVariant = Color(0xFF003EB3),
            background = Color(0xFFF6EEE5),
            surface = Color(0xFFFFFAFA),
            error = Color(0xFFD32F2F),
            onPrimary = Color(0xFFFAFAFA),
            onSecondary = Color.White,
            onBackground = Color(0xFF121212),
            onSurface = Color(0xFF1E1E1E),
            onError = Color.White
        )
    }

    val customShapes = Shapes(
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(12.dp),
        large = RoundedCornerShape(24.dp)
    )

    val customTypography = Typography(
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
            color = if (isDarkTheme) Color(0xFFCCCCCC) else Color(0xFF1E1E1E),
            fontFamily = robotoSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp
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
            color = if (isDarkTheme) Color(0xFFCCCCCC) else Color(0xFF1E1E1E),
            fontFamily = nunito,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            letterSpacing = 0.5.sp
        ),
        body2 = TextStyle(
            color = if (isDarkTheme) Color(0xFFCCCCCC) else Color(0xFF1E1E1E),
            fontFamily = nunito,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            letterSpacing = 0.25.sp
        ),
        button = TextStyle(
            color = MaterialTheme.colors.onPrimary,
            fontFamily = nunito,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            letterSpacing = 1.25.sp
        ),
        caption = TextStyle(
            color = if (isDarkTheme) Color(0xFFCCCCCC) else Color(0xFF1E1E1E),
            fontFamily = nunito,
            fontWeight = FontWeight.Black,
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
