package es.gaspardev.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import es.gaspardev.core.domain.entities.comunication.Conversation

object ConversationsState {
   private var _state: List<Conversation> by mutableStateOf(listOf())
   val state get() = _state

   fun loadConversations(conversations: List<Conversation>) {
      this._state = conversations
   }
}