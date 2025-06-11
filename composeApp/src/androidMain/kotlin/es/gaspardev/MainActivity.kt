package es.gaspardev

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import es.gaspardev.ui.screens.NavGraphs
import es.gaspardev.ui.screens.auth.AuthScreen
import es.gaspardev.utils.DeepLinkManager
import fit_me.composeapp.generated.resources.Nunito_Variable
import fit_me.composeapp.generated.resources.Res
import fit_me.composeapp.generated.resources.RobotoSerif_VariableFont
import org.jetbrains.compose.resources.Font

class MainActivity : ComponentActivity() {

    private lateinit var deepLinkManager: DeepLinkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        deepLinkManager = DeepLinkManager()

        setContent {
            MobileAppTheme {
                var deepLinkData by remember { mutableStateOf<DeepLinkManager.DeepLinkData?>(null) }

                // Procesar deep link del intent inicial
                LaunchedEffect(Unit) {
                    intent?.let { intent ->
                        deepLinkData = deepLinkManager.processDeepLink(intent)
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AuthScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        deepLinkData = deepLinkData
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        // Procesar nuevo deep link
        val deepLinkData = deepLinkManager.processDeepLink(intent)

        // Aquí puedes manejar el deep link
        // Por ejemplo, navegar a la pantalla correspondiente
        deepLinkData?.let { data ->
            handleDeepLink(data)
        }
    }

    private fun handleDeepLink(data: DeepLinkManager.DeepLinkData) {
        when (data.action) {
            DeepLinkManager.DeepLinkAction.REGISTER -> {
                // Navegar a registro con el token
                // Esto se manejará en el AuthScreen
            }

            DeepLinkManager.DeepLinkAction.LOGIN -> {
                // Navegar a login
            }

            DeepLinkManager.DeepLinkAction.UNKNOWN -> {
                // Manejar deep link desconocido
            }
        }
    }
}

@Composable
fun FitMeApp() {
    val navController = rememberNavController()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        DestinationsNavHost(
            navGraph = NavGraphs.root,
            navController = navController
        )
    }
}

@Composable
fun MobileAppTheme(
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

    val customColorScheme = if (isDarkTheme) {
        darkColorScheme(
            primary = Color(0xFFFF5722),
            onPrimary = Color(0xFFFAFAFA),
            primaryContainer = Color(0xFFE64A19),
            onPrimaryContainer = Color(0xFFFAFAFA),
            secondary = Color(0xFF448AFF),
            onSecondary = Color.White,
            secondaryContainer = Color(0xFF2962FF),
            onSecondaryContainer = Color.White,
            tertiary = Color(0xFF81C784),
            onTertiary = Color(0xFF1B5E20),
            tertiaryContainer = Color(0xFF4CAF50),
            onTertiaryContainer = Color.White,
            error = Color(0xFFCF6679),
            onError = Color.Black,
            errorContainer = Color(0xFFB71C1C),
            onErrorContainer = Color.White,
            background = Color(0xFF121212),
            onBackground = Color(0xFFE0E0E0),
            surface = Color(0xFF1E1E1E),
            onSurface = Color(0xFFCCCCCC),
            surfaceVariant = Color(0xFF2C2C2C),
            onSurfaceVariant = Color(0xFFB0B0B0),
            outline = Color(0xFF737373),
            outlineVariant = Color(0xFF404040),
            scrim = Color(0xFF000000),
            inverseSurface = Color(0xFFE0E0E0),
            inverseOnSurface = Color(0xFF121212),
            inversePrimary = Color(0xFFFF4000),
            surfaceDim = Color(0xFF0F0F0F),
            surfaceBright = Color(0xFF2A2A2A),
            surfaceContainerLowest = Color(0xFF0A0A0A),
            surfaceContainerLow = Color(0xFF171717),
            surfaceContainer = Color(0xFF1E1E1E),
            surfaceContainerHigh = Color(0xFF252525),
            surfaceContainerHighest = Color(0xFF2C2C2C)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFFFF4000),
            onPrimary = Color(0xFFFAFAFA),
            primaryContainer = Color(0xFFE03A00),
            onPrimaryContainer = Color(0xFFFAFAFA),
            secondary = Color(0xFF005DFF),
            onSecondary = Color.White,
            secondaryContainer = Color(0xFF003EB3),
            onSecondaryContainer = Color.White,
            tertiary = Color(0xFF4CAF50),
            onTertiary = Color.White,
            tertiaryContainer = Color(0xFF81C784),
            onTertiaryContainer = Color(0xFF1B5E20),
            error = Color(0xFFD32F2F),
            onError = Color.White,
            errorContainer = Color(0xFFFFEBEE),
            onErrorContainer = Color(0xFFB71C1C),
            background = Color(0xFFF6EEE5),
            onBackground = Color(0xFF121212),
            surface = Color(0xFFFFFAFA),
            onSurface = Color(0xFF1E1E1E),
            surfaceVariant = Color(0xFFF5F5F5),
            onSurfaceVariant = Color(0xFF424242),
            outline = Color(0xFF9E9E9E),
            outlineVariant = Color(0xFFE0E0E0),
            scrim = Color(0xFF000000),
            inverseSurface = Color(0xFF1E1E1E),
            inverseOnSurface = Color(0xFFE0E0E0),
            inversePrimary = Color(0xFFFF5722),
            surfaceDim = Color(0xFFEEEEEE),
            surfaceBright = Color(0xFFFFFFFF),
            surfaceContainerLowest = Color(0xFFFFFFFF),
            surfaceContainerLow = Color(0xFFFAFAFA),
            surfaceContainer = Color(0xFFF5F5F5),
            surfaceContainerHigh = Color(0xFFF0F0F0),
            surfaceContainerHighest = Color(0xFFEBEBEB)
        )
    }

    val customShapes = Shapes(
        extraSmall = RoundedCornerShape(4.dp),
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(12.dp),
        large = RoundedCornerShape(16.dp),
        extraLarge = RoundedCornerShape(28.dp)
    )

    val customTypography = Typography(
        displayLarge = TextStyle(
            fontFamily = robotoSerif,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 57.sp,
            lineHeight = 64.sp,
            letterSpacing = (-0.25).sp
        ),
        displayMedium = TextStyle(
            fontFamily = robotoSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 45.sp,
            lineHeight = 52.sp,
            letterSpacing = 0.sp
        ),
        displaySmall = TextStyle(
            fontFamily = robotoSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            lineHeight = 44.sp,
            letterSpacing = 0.sp
        ),
        headlineLarge = TextStyle(
            fontFamily = robotoSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = 0.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = robotoSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            letterSpacing = 0.sp
        ),
        headlineSmall = TextStyle(
            color = if (isDarkTheme) Color(0xFFCCCCCC) else Color(0xFF1E1E1E),
            fontFamily = robotoSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp
        ),
        titleLarge = TextStyle(
            fontFamily = robotoSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        ),
        titleMedium = TextStyle(
            fontFamily = robotoSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp
        ),
        titleSmall = TextStyle(
            fontFamily = robotoSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),
        labelLarge = TextStyle(
            fontFamily = nunito,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),
        labelMedium = TextStyle(
            fontFamily = nunito,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        ),
        labelSmall = TextStyle(
            fontFamily = nunito,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        ),
        bodyLarge = TextStyle(
            color = if (isDarkTheme) Color(0xFFCCCCCC) else Color(0xFF1E1E1E),
            fontFamily = nunito,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),
        bodyMedium = TextStyle(
            color = if (isDarkTheme) Color(0xFFCCCCCC) else Color(0xFF1E1E1E),
            fontFamily = nunito,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp
        ),
        bodySmall = TextStyle(
            color = if (isDarkTheme) Color(0xFFCCCCCC) else Color(0xFF1E1E1E),
            fontFamily = nunito,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp
        )
    )

    MaterialTheme(
        colorScheme = customColorScheme,
        typography = customTypography,
        shapes = customShapes,
        content = content
    )
}