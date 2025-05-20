package es.gaspardev.pages


import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.runtime.Composable
import es.gaspardev.core.LocalRouter
import es.gaspardev.layout.athletes.SportsmanCard
import es.gaspardev.states.LoggedTrainer


fun test() {}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AthletesScreen() {

    val controller = LocalRouter.current

    FlowColumn {
        LoggedTrainer.state.trainer!!.sportmans.forEach {
            SportsmanCard(it)
        }
    }

}
