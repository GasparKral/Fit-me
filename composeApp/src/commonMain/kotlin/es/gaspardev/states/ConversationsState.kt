package es.gaspardev.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import es.gaspardev.core.domain.entities.comunication.Conversation
import es.gaspardev.core.domain.entities.users.User

object ConversationsState {
   private var _state: List<Conversation> by mutableStateOf(emptyList())
   private var _selection: Conversation? by mutableStateOf(null)

   val selection get() = _selection
   val state get() = _state

   fun loadConversations(conversations: List<Conversation>) {
      this._state = conversations
      this._selection = conversations.first()
   }

   fun selectConversation(user: User) {
      _selection = _state.first { it.athlete.id == user.id || it.trainer.id == user.id }
   }
}