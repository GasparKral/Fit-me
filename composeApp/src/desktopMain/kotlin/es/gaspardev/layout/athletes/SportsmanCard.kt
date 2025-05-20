package es.gaspardev.layout.athletes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import es.gaspardev.components.ProgressBar
import es.gaspardev.core.LocalRouter
import es.gaspardev.core.domain.entities.Sportsman
import es.gaspardev.pages.Routes

@Composable
fun SportsmanCard(athlete: Sportsman) {
    val controller = LocalRouter.current

    Card() {
        Row() {
            Column() {
                Text(athlete.user.name)
                Text(athlete.user.email)
                ProgressBar(value = 70.0, label = { Text("Progreso") })
                Button(
                    onClick = { controller.navigateTo(Routes.AthleteInfo) }
                ) {
                    Text("Ir a ver informacion")
                }
            }
        }
    }
}