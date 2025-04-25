package es.gaspardev.core.Routing

import androidx.compose.runtime.*
import es.gaspardev.core.actions.PageAction
import es.gaspardev.pages.*
import kotlin.reflect.KFunction1

@Composable
fun Router(box: @Composable (content: @Composable () -> Unit) -> Unit): RouterController {
    var actualRoute by remember { mutableStateOf<Route>(Route.Login) }
    val history = remember { mutableStateListOf<Route>() }

    fun navigateTo(route: Route) {
        history.add(actualRoute)
        actualRoute = route
    }

    fun navigateToWithAcction(route: Route, action: PageAction, vararg actionParams: Any?) {
        history.add(actualRoute)
        actualRoute = route
        action.execute(actionParams)
    }

    fun goBack() {
        if (history.isNotEmpty()) {
            actualRoute = history.removeLast()
        }
    }

    val controller = RouterController(
        ::navigateTo,
        ::navigateToWithAcction,
        ::goBack,
        rememberUpdatedState(actualRoute)
    )

    // Renderizar la vista activa dentro del contenedor box
    box {
        when (actualRoute) {
            is Route.Login -> LoginScreen(controller)
            is Route.Dashboard -> DashboardScreen(controller)
            is Route.Athletes -> AthletesScreen(controller)
            is Route.Workouts -> WorkoutsScreen(controller)
            is Route.Nutrition -> NutritionScreen(controller)
            is Route.Calendar -> CalendarScreen(controller)
            is Route.Messages -> MessagesScreen(controller)
            is Route.Statistics -> StatisticsScreen(controller)
            is Route.Settings -> SettingsScreen(controller)
            is Route.Regist -> RegistScreen(controller)
        }
    }

    return controller
}


@Stable
data class RouterController(
    val navigateTo: KFunction1<Route, Unit>,
    val navigateToWithAcction: (Route, PageAction, Array<out Any?>) -> Unit,
    val goBack: () -> Unit,
    val currentRoute: State<Route>
)
