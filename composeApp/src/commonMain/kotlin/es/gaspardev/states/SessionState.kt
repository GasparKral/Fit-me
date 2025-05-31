package es.gaspardev.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import es.gaspardev.core.domain.entities.comunication.Session

object SessionState {
    private var _state: List<Session> by mutableStateOf(listOf())
    val state get() = _state
}