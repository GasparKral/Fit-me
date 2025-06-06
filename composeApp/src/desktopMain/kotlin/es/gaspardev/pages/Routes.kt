package es.gaspardev.pages

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import es.gaspardev.core.Route
import es.gaspardev.core.domain.entities.users.Athlete

sealed class Routes {
    object Login : Route {
        override val view: @Composable (Dp, Dp) -> Unit
            get() = { _, _ -> LoginScreen() }
    }

    object Dashboard : Route {
        override val view: @Composable (Dp, Dp) -> Unit
            get() = { _, _ -> DashboardScreen() }

    }

    object Regist : Route {
        override val view: @Composable (Dp, Dp) -> Unit
            get() = { _, _ -> RegistScreen() }
    }

    object Athletes : Route {
        override val view: @Composable (Dp, Dp) -> Unit
            get() = { _, height -> AthletesScreen(height) }

    }

    object Calendar : Route {
        override val view: @Composable (Dp, Dp) -> Unit
            get() = { _, _ -> CalendarScreen() }
    }

    object Messages : Route {
        override val view: @Composable (Dp, Dp) -> Unit
            get() = { _, _ -> MessagesScreen() }
    }

    object Nutrition : Route {
        override val view: @Composable (Dp, Dp) -> Unit
            get() = { width, _ -> NutritionScreen(width) }
    }

    object Settings : Route {
        override val view: @Composable (Dp, Dp) -> Unit
            get() = { _, _ -> SettingsScreen() }
    }

    object Statistics : Route {
        override val view: @Composable (Dp, Dp) -> Unit
            get() = { _, _ -> StatisticsScreen() }
    }

    object Workouts : Route {
        override val view: @Composable (Dp, Dp) -> Unit
            get() = { width, _ -> WorkoutsScreen(width) }
    }

    object AthleteInfo : Route {

        fun load(athlete: Athlete): AthleteInfo {
            this.athlete = athlete
            return this
        }

        lateinit var athlete: Athlete
        override val view: @Composable (Dp, Dp) -> Unit
            get() = { _, _ -> AthleteInfoScreen(athlete) }
    }
}
