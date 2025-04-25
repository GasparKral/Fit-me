package es.gaspardev.core.Routing

sealed class Route {
    object Login : Route()
    object Regist : Route()
    object Dashboard : Route()
    object Athletes : Route()
    object Workouts : Route()
    object Nutrition : Route()
    object Calendar : Route()
    object Messages : Route()
    object Statistics : Route()
    object Settings : Route()
}
