package es.gaspardev.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.entities.users.Trainer

data class LoggedUserState(
    var trainer: Trainer? = null,
    var athletes: List<Athlete>? = null
)

object LoggedTrainer {
    private var _state by mutableStateOf(LoggedUserState())
    val state: LoggedUserState get() = _state


    fun login(trainer: Trainer, athletes: List<Athlete>) {
        _state = LoggedUserState(
            trainer = trainer,
            athletes = athletes
        )
    }

    fun logout() {
        _state = LoggedUserState()
    }

    fun updateTrainer(trainer: Trainer) {
        _state = _state.copy(trainer = trainer)
    }

}