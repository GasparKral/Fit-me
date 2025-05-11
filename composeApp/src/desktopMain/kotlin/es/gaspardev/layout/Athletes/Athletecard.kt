package es.gaspardev.layout.Athletes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import es.gaspardev.components.ProgressBar
import es.gaspardev.core.domain.entities.Sportsman

@Composable
fun AthleteCard(athlete: Sportsman) {
    Card() {
        Row() {
            Column() {
                Text(athlete.user.name)
                Text(athlete.user.email)
                ProgressBar(value = 70.0, label = { Text("Progreso") })
            }
            Column() {}
        }
    }
}