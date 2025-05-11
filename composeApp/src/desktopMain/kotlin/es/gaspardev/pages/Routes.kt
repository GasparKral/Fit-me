package es.gaspardev.pages

import androidx.compose.runtime.Composable
import es.gaspardev.core.Route

sealed class Routes {
    object Login : Route {
        override val view: @Composable () -> Unit
            get() = { LoginScreen() }
    }

    object Dashboard : Route {
        override val view: @Composable () -> Unit
            get() = { DashboardScreen() }

    }

    object Regist : Route {
        override val view: @Composable () -> Unit
            get() = { RegistScreen() }
    }

    object Athletes : Route {
        override val view: @Composable () -> Unit
            get() = { AthletesScreen() }

    }

    object Calendar : Route {
        override val view: @Composable () -> Unit
            get() = { CalendarScreen() }
    }

    object Messages : Route {
        override val view: @Composable () -> Unit
            get() = { MessagesScreen() }
    }

    object Nutrition : Route {
        override val view: @Composable () -> Unit
            get() = { NutritionScreen() }
    }

    object Settings : Route {
        override val view: @Composable () -> Unit
            get() = { SettingsScreen() }
    }

    object Statistics : Route {
        override val view: @Composable () -> Unit
            get() = { StatisticsScreen() }
    }

    object Workouts : Route {
        override val view: @Composable () -> Unit
            get() = { WorkoutsScreen() }
    }
}
