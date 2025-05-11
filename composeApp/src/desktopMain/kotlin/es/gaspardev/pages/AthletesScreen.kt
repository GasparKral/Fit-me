package es.gaspardev.pages


import androidx.compose.runtime.Composable
import es.gaspardev.core.LocalRouter
import es.gaspardev.core.domain.entities.Sportsman
import es.gaspardev.core.domain.entities.SportsmanStatus
import es.gaspardev.core.domain.entities.User
import es.gaspardev.layout.Athletes.AthleteCard
import kotlinx.datetime.Clock


fun test() {}

@Composable
fun AthletesScreen() {

    val controller = LocalRouter.current

    AthleteCard(
        athlete = Sportsman(
            User(
                id = 4,
                name = "Mario",
                password = "",
                email = "tupapideconfi@gmail.com",
                creationTime = Clock.System.now(),
                userImage = null
            ),
            trainer = null,
            age = 32,
            weight = null,
            height = null,
            sex = false,
            allergies = null,
            workouts = null,
            diet = null,
            suplementation = null,
            status = SportsmanStatus(
                status = true,
                lastActive = Clock.System.now(),
                needsAttetion = false
            ),
        )
    )

}
