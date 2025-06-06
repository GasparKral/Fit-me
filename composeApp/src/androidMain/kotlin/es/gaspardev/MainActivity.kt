package es.gaspardev

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.DestinationsNavHost
import es.gaspardev.ui.screens.NavGraphs
import es.gaspardev.ui.screens.destinations.DashboardScreenDestination
import es.gaspardev.ui.screens.destinations.LoginScreenDestination

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                AppNavigation(
                    isUserLoggedIn = checkIfUserIsLoggedIn()
                )
            }
        }
    }

    private fun checkIfUserIsLoggedIn(): Boolean {
        return true //TODO CAMBIAR
    }
}

@Composable
fun AppNavigation(
    isUserLoggedIn: Boolean = false
) {
    DestinationsNavHost(
        navGraph = NavGraphs.root,
        startRoute = if (isUserLoggedIn) DashboardScreenDestination else LoginScreenDestination
    )
}