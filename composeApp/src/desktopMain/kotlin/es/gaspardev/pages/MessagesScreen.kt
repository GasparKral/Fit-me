package es.gaspardev.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import es.gaspardev.components.UserAvatar
import es.gaspardev.components.MessageBubble
import es.gaspardev.core.domain.entities.comunication.Conversation
import es.gaspardev.enums.MessageStatus
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.messages.ConversationItem
import es.gaspardev.states.ConversationsState
import es.gaspardev.core.infrastructure.shockets.ChatWebShocket
import es.gaspardev.core.domain.entities.comunication.Message
import es.gaspardev.states.LoggedTrainer
import fit_me.composeapp.generated.resources.Res
import fit_me.composeapp.generated.resources.all
import fit_me.composeapp.generated.resources.unread_messages
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun MessagesScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedConversation by remember { mutableStateOf<Conversation?>(null) }
    var isModalOpen by remember { mutableStateOf(false) }
    var messageText by remember { mutableStateOf("") }

    // WebSocket states
    var webSocket by remember { mutableStateOf<ChatWebShocket?>(null) }
    var isConnected by remember { mutableStateOf(false) }
    var currentMessages by remember { mutableStateOf<List<Message>>(listOf()) }
    var connectionError by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    // WebSocket connection management
    fun connectToConversation(conversation: Conversation) {
        // Limpiar estado anterior
        webSocket?.dispose()
        connectionError = null
        isConnected = false
        currentMessages = emptyList()
        messageText = "" // Limpiar el input de texto

        webSocket = ChatWebShocket(
            userId = LoggedTrainer.state.trainer!!.user.id.toString(),
            conversationId = conversation.id,
            onMessageReceived = { message ->
                // Evitar duplicados - verificar si el mensaje ya existe
                val messageExists = currentMessages.any {
                    it.sendAt == message.sendAt && it.userName == message.userName && it.content == message.content
                }
                if (!messageExists) {
                    currentMessages = currentMessages + message
                }
            },
            onStatusUpdate = { status ->
                // TODO: Actualizar estado específico del mensaje
                println("Message status updated: $status")
                // Nota: Aquí necesitarías más información para identificar qué mensaje actualizar
                // Por ahora solo lo logueamos
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

        // Cargar mensajes existentes de la conversación primero
        currentMessages = conversation.messages.toList()

        // Luego conectar al WebSocket
        webSocket?.connect()
    }

    fun handleSendMessage() {
        if (messageText.trim().isEmpty() || !isConnected) return

        scope.launch {
            val success = webSocket?.sendMessage(messageText.trim()) ?: false
            if (success) {
                messageText = ""
            } else {
                connectionError = "Failed to send message. Check your connection."
            }
        }
    }

    // Cleanup cuando se cierre la pantalla
    DisposableEffect(Unit) {
        onDispose {
            webSocket?.dispose()
        }
    }

    val tabs = listOf(
        MessageStatus.ALL to stringResource(Res.string.all),
        MessageStatus.DELIVERED to stringResource(Res.string.unread_messages),
        MessageStatus.READ to "Leidos"
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
                            .height(24.dp),
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
                        items(ConversationsState.state.filter { it ->
                            when (activeTab) {
                                MessageStatus.ALL -> {
                                    true
                                }

                                MessageStatus.DELIVERED -> {
                                    it.messages.minByOrNull { it.sendAt }!!.messageStatus == MessageStatus.DELIVERED
                                }

                                else -> {
                                    it.messages.minByOrNull { it.sendAt }!!.messageStatus == MessageStatus.READ
                                }
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
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Header
                        Column {
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
                                        UserAvatar(selectedConversation!!.trainer)
                                        // Indicator de conexión WebSocket
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    when {
                                                        isConnected -> Color.Green
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
                                            text = selectedConversation!!.trainer.fullname,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = when {
                                                connectionError != null -> "Connection error"
                                                else -> webSocket?.getConnectionStatus() ?: "Disconnected"
                                            },
                                            fontSize = 12.sp,
                                            color = when {
                                                isConnected -> Color.Green
                                                connectionError != null -> Color.Red
                                                else -> MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                            }
                                        )
                                    }
                                }
                                Row {
                                    // Reconnect button if there's an error
                                    if (connectionError != null) {
                                        IconButton(
                                            onClick = {
                                                selectedConversation?.let { connectToConversation(it) }
                                            }
                                        ) {
                                            Icon(Icons.Default.Refresh, contentDescription = "Reconnect")
                                        }
                                    }
                                    IconButton(onClick = { /* Handle call */ }) {
                                        Icon(Icons.Default.Call, contentDescription = "Call")
                                    }
                                    IconButton(onClick = { /* Handle video */ }) {
                                        Icon(FitMeIcons.Messages, contentDescription = "Video")
                                    }
                                    IconButton(onClick = { /* Handle info */ }) {
                                        Icon(Icons.Default.Info, contentDescription = "Info")
                                    }
                                    IconButton(onClick = { /* Handle more */ }) {
                                        Icon(Icons.Default.MoreVert, contentDescription = "More")
                                    }
                                }
                            }

                            // Error banner
                            connectionError?.let { error ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    backgroundColor = Color.Red.copy(alpha = 0.1f),
                                    border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.3f))
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
                                        TextButton(
                                            onClick = { connectionError = null }
                                        ) {
                                            Text("Dismiss", fontSize = 12.sp)
                                        }
                                    }
                                }
                                Spacer(Modifier.height(8.dp))
                            }
                        }

                        if (connectionError == null) {
                            Divider()
                        }

                        // Messages
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(currentMessages) { message ->
                                MessageBubble(message = message)
                            }
                        }

                        // Message input
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = messageText,
                                onValueChange = { messageText = it },
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
                                    onSend = { if (isConnected) handleSendMessage() }
                                ),
                                trailingIcon = {
                                    Row {
                                        IconButton(
                                            onClick = { /* Handle attachment */ },
                                            enabled = isConnected
                                        ) {
                                            Icon(
                                                Icons.Default.AccountBox,
                                                contentDescription = "Attach",
                                                tint = if (isConnected)
                                                    MaterialTheme.colors.onSurface
                                                else
                                                    MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
                                            )
                                        }
                                    }
                                }
                            )
                            Spacer(Modifier.width(8.dp))
                            Button(
                                onClick = { handleSendMessage() },
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
