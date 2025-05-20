package es.gaspardev.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import es.gaspardev.core.domain.entities.Sportsman
import es.gaspardev.core.domain.entities.Trainer
import es.gaspardev.core.domain.entities.User

data class LoggedUserState(
    var trainer: Trainer? = null,
)

object LoggedTrainer {
    private var _state by mutableStateOf(LoggedUserState())
    val state: LoggedUserState get() = _state

    fun login(user: User, trainer: Trainer?, sportsman: Sportsman?) {
        _state = LoggedUserState(
            trainer = trainer,
        )
    }

    fun logout() {
        _state = LoggedUserState()
    }

    fun updateTrainer(trainer: Trainer) {
        _state = _state.copy(trainer = trainer)
    }

}