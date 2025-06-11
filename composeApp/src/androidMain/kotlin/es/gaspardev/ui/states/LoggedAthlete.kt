package es.gaspardev.ui.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.entities.users.Trainer

public data class LoggedAthleteState(
    private var _athlete: Athlete? = null,
    private var _trainer: Trainer? = null
) {
    public val athlete get() = _athlete!!
    public val trainer get() = _trainer!!

    fun logIn(athlete: Athlete, trainer: Trainer) {
        _athlete = athlete
        _trainer = trainer
    }
}

object LoggedAthlete {
    public var state: LoggedAthleteState by mutableStateOf(LoggedAthleteState())
    public fun logIn(athlete: Athlete, trainer: Trainer) {
        this.state.logIn(athlete, trainer)
    }

    public fun logOut() {
        this.state = LoggedAthleteState()
    }
}