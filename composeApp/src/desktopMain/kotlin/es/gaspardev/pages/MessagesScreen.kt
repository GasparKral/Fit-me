package es.gaspardev.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.gaspardev.components.MessageBubble
import es.gaspardev.components.UserAvatar
import es.gaspardev.core.domain.entities.comunication.Conversation
import es.gaspardev.core.domain.entities.comunication.Message
import es.gaspardev.core.domain.usecases.read.GetConversations
import es.gaspardev.core.infrastructure.repositories.TrainerRepositoryImp
import es.gaspardev.core.infrastructure.shockets.ChatWebSocket
import es.gaspardev.core.infrastructure.shockets.WebSocketMessage
import es.gaspardev.enums.MessageStatus
import es.gaspardev.enums.MessageType
import es.gaspardev.layout.messages.ConversationItem
import es.gaspardev.states.ConversationsState
import es.gaspardev.states.LoggedTrainer
import fit_me.composeapp.generated.resources.Res
import fit_me.composeapp.generated.resources.all
import fit_me.composeapp.generated.resources.unread_messages
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.stringResource
import java.util.*

@Composable
fun MessagesScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedConversation by remember { mutableStateOf<Conversation?>(null) }
    var isModalOpen by remember { mutableStateOf(false) }
    var messageText by remember { mutableStateOf("") }

    // WebSocket states mejorados
    var webSocket by remember { mutableStateOf<ChatWebSocket?>(null) }
    var isConnected by remember { mutableStateOf(false) }
    var currentMessages by remember { mutableStateOf<List<Message>>(listOf()) }
    var connectionError by remember { mutableStateOf<String?>(null) }
    var isOtherUserOnline by remember { mutableStateOf(false) }
    var isOtherUserTyping by remember { mutableStateOf(false) }
    var pendingMessages by remember { mutableStateOf<Map<String, Message>>(mapOf()) }

    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    // Función para auto-scroll al final
    fun scrollToBottom() {
        scope.launch {
            if (currentMessages.isNotEmpty()) {
                listState.animateScrollToItem(currentMessages.size - 1)
            }
        }
    }

    // WebSocket connection management mejorado
    fun connectToConversation(conversation: Conversation) {
        webSocket?.disconnect()
        connectionError = null
        isConnected = false
        currentMessages = emptyList()
        messageText = ""
        isOtherUserOnline = false
        isOtherUserTyping = false
        pendingMessages = emptyMap()

        val currentUserId = LoggedTrainer.state.trainer?.user?.id ?: return

        webSocket = ChatWebSocket(
            userId = currentUserId,
            conversationId = conversation.id,
            onWebSocketMessage = { wsMessage ->
                when (wsMessage) {
                    is WebSocketMessage.MessageReceived -> {
                        val message = wsMessage.message

                        // Remover de pendientes si existe
                        val tempId = pendingMessages.entries.find { it.value.id == message.id }?.key
                        if (tempId != null) {
                            pendingMessages = pendingMessages - tempId
                        }

                        // Actualizar o agregar mensaje
                        currentMessages = currentMessages.filter { it.id != message.id } + message
                        currentMessages = currentMessages.sortedBy { it.sentAt }

                        // Auto-scroll si es un nuevo mensaje
                        scrollToBottom()

                        // Marcar como entregado si no es del usuario actual
                        if (message.senderId != currentUserId && message.messageStatus == MessageStatus.SENT) {
                            scope.launch {
                                webSocket?.sendStatusUpdate(message.id, MessageStatus.DELIVERED)
                            }
                        }
                    }

                    is WebSocketMessage.MessageStatusUpdate -> {
                        currentMessages = currentMessages.map { msg ->
                            if (msg.id == wsMessage.messageId) {
                                msg.copy(
                                    messageStatus = wsMessage.status,
                                    deliveredAt = if (wsMessage.status == MessageStatus.DELIVERED) wsMessage.timestamp else msg.deliveredAt,
                                    readAt = if (wsMessage.status == MessageStatus.READ) wsMessage.timestamp else msg.readAt
                                )
                            } else msg
                        }
                    }

                    is WebSocketMessage.UserOnlineStatus -> {
                        if (wsMessage.userId != currentUserId) {
                            isOtherUserOnline = wsMessage.isOnline
                        }
                    }

                    is WebSocketMessage.TypingIndicator -> {
                        if (wsMessage.userId != currentUserId) {
                            isOtherUserTyping = wsMessage.isTyping
                        }
                    }

                    is WebSocketMessage.Error -> {
                        connectionError = wsMessage.message

                        // Marcar mensaje pendiente como fallido si hay tempId
                        wsMessage.tempId?.let { tempId ->
                            pendingMessages[tempId]?.let { failedMessage ->
                                currentMessages = currentMessages.map { msg ->
                                    if (msg.id == failedMessage.id) {
                                        msg.copy(messageStatus = MessageStatus.FAILED)
                                    } else msg
                                }
                                pendingMessages = pendingMessages - tempId
                            }
                        }
                    }

                    else -> {
                        // Handle other message types
                    }
                }
            },
            onConnectionEvent = { connected ->
                isConnected = connected
                if (connected) {
                    connectionError = null
                }
            },
            onError = { error ->
                connectionError = error
                println("WebSocket error: $error")
            }
        )

        // Cargar mensajes existentes
        currentMessages = conversation.messages.sortedBy { it.sentAt }
        webSocket?.connect()

        // Auto-scroll inicial
        scrollToBottom()
    }

    fun handleSendMessage() {
        if (messageText.trim().isEmpty() || !isConnected || selectedConversation == null) return

        scope.launch {
            val tempId = UUID.randomUUID().toString()
            val messageId = UUID.randomUUID().toString()
            val now = Clock.System.now()
            val currentUserId = LoggedTrainer.state.trainer?.user?.id ?: return@launch

            // Determinar receptor
            val receiverId = if (selectedConversation!!.trainer.id == currentUserId) {
                selectedConversation!!.athlete.id
            } else {
                selectedConversation!!.trainer.id
            }

            // Crear mensaje local inmediatamente
            val localMessage = Message(
                id = messageId,
                conversationId = selectedConversation!!.id,
                senderId = currentUserId,
                senderName = LoggedTrainer.state.trainer?.user?.fullname ?: "",
                receiverId = receiverId,
                content = messageText.trim(),
                messageType = MessageType.TEXT,
                sentAt = now,
                messageStatus = MessageStatus.SENDING
            )

            // Agregar a la lista inmediatamente
            currentMessages = currentMessages + localMessage
            pendingMessages = pendingMessages + (tempId to localMessage)

            // Auto-scroll
            scrollToBottom()

            val success = webSocket?.sendMessage(
                content = messageText.trim(),
                messageType = MessageType.TEXT,
                tempId = tempId
            ) ?: false

            if (success) {
                messageText = ""
                // Actualizar estado a SENT
                currentMessages = currentMessages.map { msg ->
                    if (msg.id == messageId) {
                        msg.copy(messageStatus = MessageStatus.SENT)
                    } else msg
                }
            } else {
                connectionError = "Failed to send message. Check your connection."
                // Marcar mensaje como fallido
                currentMessages = currentMessages.map { msg ->
                    if (msg.id == messageId) {
                        msg.copy(messageStatus = MessageStatus.FAILED)
                    } else msg
                }
                pendingMessages = pendingMessages - tempId
            }
        }
    }

    fun handleRetryMessage(messageId: String) {
        val failedMessage = currentMessages.find { it.id == messageId }
        if (failedMessage?.messageStatus == MessageStatus.FAILED) {
            scope.launch {
                val tempId = UUID.randomUUID().toString()

                // Actualizar estado a enviando
                currentMessages = currentMessages.map { msg ->
                    if (msg.id == messageId) {
                        msg.copy(messageStatus = MessageStatus.SENDING)
                    } else msg
                }

                val success = webSocket?.sendMessage(
                    content = failedMessage.content,
                    messageType = failedMessage.messageType,
                    tempId = tempId
                ) ?: false

                if (success) {
                    // Actualizar a SENT
                    currentMessages = currentMessages.map { msg ->
                        if (msg.id == messageId) {
                            msg.copy(messageStatus = MessageStatus.SENT)
                        } else msg
                    }
                } else {
                    // Volver a FAILED
                    currentMessages = currentMessages.map { msg ->
                        if (msg.id == messageId) {
                            msg.copy(messageStatus = MessageStatus.FAILED)
                        } else msg
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        GetConversations(TrainerRepositoryImp()).run(LoggedTrainer.state.trainer!!.user).fold(
            { res -> ConversationsState.loadConversations(res) },
            { _ -> }
        )
    }

    // Typing indicator
    LaunchedEffect(messageText) {
        if (isConnected && selectedConversation != null) {
            val isTyping = messageText.isNotEmpty()
            webSocket?.sendTypingIndicator(selectedConversation!!.id, isTyping)
        }
    }

    // Cleanup cuando se cambia la pantalla
    DisposableEffect(Unit) {
        onDispose {
            webSocket?.disconnect()
        }
    }

    val tabs = listOf(
        MessageStatus.ALL to stringResource(Res.string.all),
        MessageStatus.DELIVERED to stringResource(Res.string.unread_messages),
        MessageStatus.READ to "Leídos"
    )

    var activeTab by remember { mutableStateOf(MessageStatus.ALL) }

    Box(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxSize()) {
            Card(
                shape = RectangleShape,
                modifier = Modifier
                    .width(300.dp).fillMaxHeight()
            ) {
                // Conversations sidebar
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Messages",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Button(
                            onClick = { isModalOpen = true },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = MaterialTheme.colors.onPrimary
                            )
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "New")
                            Spacer(Modifier.width(4.dp))
                            Text("New")
                        }
                    }

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                            .height(56.dp),
                        placeholder = { Text("Search conversations...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        },
                        singleLine = true
                    )

                    Spacer(Modifier.height(8.dp))

                    TabRow(
                        selectedTabIndex = tabs.indexOfFirst { it.first == activeTab }.takeIf { it >= 0 } ?: 0,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        tabs.forEachIndexed { _, (type, title) ->
                            Tab(
                                selected = activeTab == type,
                                onClick = { activeTab = type },
                                text = { Text(title) }
                            )
                        }
                    }

                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(ConversationsState.state.filter { conversation ->
                            when (activeTab) {
                                MessageStatus.ALL -> true
                                MessageStatus.DELIVERED -> conversation.unreadCount > 0
                                MessageStatus.READ -> conversation.unreadCount == 0
                                else -> true
                            }
                        }) { conversation ->
                            ConversationItem(
                                conversation = conversation,
                                isSelected = selectedConversation == conversation,
                                onClick = {
                                    selectedConversation = conversation
                                    connectToConversation(conversation)
                                }
                            )
                        }
                    }
                }
            }

            // Message thread
            Box(modifier = Modifier.weight(1f)) {
                if (selectedConversation != null) {
                    ChatView(
                        conversation = selectedConversation!!,
                        messages = currentMessages,
                        messageText = messageText,
                        onMessageTextChange = { messageText = it },
                        onSendMessage = ::handleSendMessage,
                        isConnected = isConnected,
                        connectionError = connectionError,
                        isOtherUserOnline = isOtherUserOnline,
                        isOtherUserTyping = isOtherUserTyping,
                        onMessageRead = { messageId ->
                            scope.launch {
                                webSocket?.sendStatusUpdate(messageId, MessageStatus.READ)
                            }
                        },
                        onRetryMessage = ::handleRetryMessage,
                        listState = listState
                    )
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No conversation selected",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Select a conversation from the list or start a new one to begin messaging.",
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                        Button(
                            onClick = { isModalOpen = true },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = MaterialTheme.colors.onPrimary
                            ),
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "New")
                            Spacer(Modifier.width(8.dp))
                            Text("New Conversation")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatView(
    conversation: Conversation,
    messages: List<Message>,
    messageText: String,
    onMessageTextChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    isConnected: Boolean,
    connectionError: String?,
    isOtherUserOnline: Boolean,
    isOtherUserTyping: Boolean,
    onMessageRead: (String) -> Unit,
    onRetryMessage: (String) -> Unit,
    listState: LazyListState
) {
    Column(modifier = Modifier.fillMaxHeight()) {
        // Header
        ChatHeader(
            conversation = conversation,
            isConnected = isConnected,
            connectionError = connectionError,
            isOtherUserOnline = isOtherUserOnline,
            isOtherUserTyping = isOtherUserTyping
        )

        // Error banner
        connectionError?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                backgroundColor = Color.Red.copy(alpha = 0.1f),
                border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.3f)),
                elevation = 0.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = "Error",
                        tint = Color.Red,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = error,
                        fontSize = 12.sp,
                        color = Color.Red,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Divider()

        // Messages
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { message ->
                MessageBubble(
                    message = message,
                    currentUserId = LoggedTrainer.state.trainer?.user?.id ?: 0,
                    onMessageRead = onMessageRead,
                    onRetryMessage = onRetryMessage
                )
            }
        }

        // Typing indicator
        if (isOtherUserTyping) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Typing...",
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        }

        Card(Modifier.padding(12.dp)) {
            // Message input
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = onMessageTextChange,
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text(
                            when {
                                !isConnected && connectionError != null -> "Connection error - cannot send messages"
                                !isConnected -> "Connecting..."
                                else -> "Type your message..."
                            }
                        )
                    },
                    enabled = isConnected,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = { if (isConnected) onSendMessage() }
                    ),
                    maxLines = 4
                )
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = onSendMessage,
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    enabled = isConnected && messageText.trim().isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (isConnected && messageText.trim().isNotEmpty())
                            MaterialTheme.colors.primary
                        else
                            MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
                        contentColor = if (isConnected && messageText.trim().isNotEmpty())
                            MaterialTheme.colors.onPrimary
                        else
                            MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
                    )
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                }
            }
        }
    }
}

@Composable
private fun ChatHeader(
    conversation: Conversation,
    isConnected: Boolean,
    connectionError: String?,
    isOtherUserOnline: Boolean,
    isOtherUserTyping: Boolean
) {
    Column() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar with online indicator
                Box {
                    // Indicator de conexión
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isConnected && isOtherUserOnline -> Color.Green
                                    connectionError != null -> Color.Red
                                    else -> Color.Gray
                                }
                            )
                            .align(Alignment.BottomEnd)
                            .border(2.dp, MaterialTheme.colors.surface, CircleShape)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = conversation.athlete.fullname,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = when {
                            connectionError != null -> "Connection error"
                            !isConnected -> "Connecting..."
                            isOtherUserTyping -> "Typing..."
                            isOtherUserOnline -> "Online"
                            else -> "Offline"
                        },
                        fontSize = 12.sp,
                        color = when {
                            isConnected && isOtherUserOnline -> Color.Green
                            connectionError != null -> Color.Red
                            else -> MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        }
                    )
                }
            }
            Row {
                IconButton(onClick = { /* Handle info */ }) {
                    Icon(Icons.Default.Info, contentDescription = "Info")
                }
                IconButton(onClick = { /* Handle more */ }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                }
            }
        }
    }
}