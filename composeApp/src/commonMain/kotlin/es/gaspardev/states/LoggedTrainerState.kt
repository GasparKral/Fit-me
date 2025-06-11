package es.gaspardev.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.domain.usecases.read.user.athlete.GetAthleteInfo

data class LoggedUserState(
    private var _trainer: Trainer? = null,
    var athletes: List<Athlete>? = null
) {
    val trainer get() = _trainer!!
    val isNull get() = _trainer == null
}

object LoggedTrainer {
    private var _state by mutableStateOf(LoggedUserState())
    val state: LoggedUserState get() = _state


    fun login(trainer: Trainer, athletes: List<Athlete>) {
        _state = LoggedUserState(
            _trainer = trainer,
            athletes = athletes
        )
    }

    fun logout() {
        _state = LoggedUserState()
    }

    suspend fun updateAthleteInfo(athleteId: Int) {
        GetAthleteInfo().run(athleteId).fold(
            { athlete -> _state.athletes = _state.athletes!!.filter { it.user.id != athleteId } + athlete }
        )
    }

}