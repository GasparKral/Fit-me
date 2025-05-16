package es.gaspardev.pages

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.gaspardev.components.UserAvatar
import es.gaspardev.core.domain.entities.User
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.ConversationItem
import es.gaspardev.layout.messages.MessageBubble
import es.gaspardev.layout.messages.MessageDialog
import kotlinx.datetime.Clock


// Data classes
data class Message(
    val id: String,
    val conversationId: String,
    val senderId: String,
    val senderName: String,
    val senderImage: String,
    val senderInitials: String,
    val content: String,
    val timestamp: String,
    val status: String
)

data class Conversation(
    val id: String,
    val name: String,
    val image: String,
    val initials: String,
    val lastMessage: String,
    val time: String,
    val unread: Boolean,
    val online: Boolean,
    val important: Boolean
)

// Sample data
val sampleMessages = listOf(
    Message(
        id = "1",
        conversationId = "1",
        senderId = "trainer",
        senderName = "You",
        senderImage = "",
        senderInitials = "TR",
        content = "Hi Carlos, how's your progress with the new workout plan?",
        timestamp = "10:00 AM",
        status = "read"
    ),
    // Add other messages...
)

val sampleConversations = listOf(
    Conversation(
        id = "1",
        name = "Carlos Rodriguez",
        image = "",
        initials = "CR",
        lastMessage = "I completed the workout plan you sent. It was great!",
        time = "10:30 AM",
        unread = true,
        online = true,
        important = false
    ),
    // Add other conversations...
)

val sampleUser = listOf(
    User(
        name = "Carlos Rodriguez",
        email = "carlos.rodriguez@example.com",
        userImage = null,
        id = 1,
        password = "DO()",
        creationTime = Clock.System.now()
    )
)

@Composable
fun MessagesScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedConversation by remember { mutableStateOf<String?>("1") }
    var isModalOpen by remember { mutableStateOf(false) }
    var messageText by remember { mutableStateOf("") }
    var activeTab by remember { mutableStateOf("all") }

    fun handleSendMessage() {
        if (messageText.trim().isEmpty()) return
        println("Sending message: $messageText")
        messageText = ""
    }

    MessageDialog(
        onDismiss = {},
        onSend = { ok, ko -> }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Conversations sidebar
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight()
                    .border(1.dp, MaterialTheme.colors.primary)
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
                        .padding(horizontal = 16.dp),
                    placeholder = { Text("Search conversations...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    },
                    singleLine = true
                )

                TabRow(
                    selectedTabIndex = when (activeTab) {
                        "all" -> 0
                        "unread" -> 1
                        "important" -> 2
                        else -> 0
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("All", "Unread", "Important").forEachIndexed { index, title ->
                        Tab(
                            selected = when (index) {
                                0 -> activeTab == "all"
                                1 -> activeTab == "unread"
                                2 -> activeTab == "important"
                                else -> false
                            },
                            onClick = {
                                activeTab = when (index) {
                                    0 -> "all"
                                    1 -> "unread"
                                    2 -> "important"
                                    else -> "all"
                                }
                            },
                            text = { Text(title) }
                        )
                    }
                }

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(sampleConversations.filter {
                        it.name.contains(searchQuery, ignoreCase = true) ||
                                it.lastMessage.contains(searchQuery, ignoreCase = true)
                    }) { conversation ->
                        ConversationItem(
                            conversation = conversation,
                            isSelected = selectedConversation == conversation.id,
                            onClick = { selectedConversation = conversation.id }
                        )
                    }
                }
            }

            // Message thread
            Box(modifier = Modifier.weight(1f)) {
                if (selectedConversation != null) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Header
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
                                    UserAvatar(sampleUser[0])
                                    if (sampleConversations.first { it.id == selectedConversation }.online) {
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .clip(CircleShape)
                                                .background(Color.Green)
                                                .align(Alignment.BottomEnd)
                                                .border(2.dp, MaterialTheme.colors.surface, CircleShape)
                                        )
                                    }
                                }
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = sampleConversations.first { it.id == selectedConversation }.name,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "Last active: Just now",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                    )
                                }
                            }
                            Row {
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

                        Divider()

                        // Messages
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(sampleMessages.filter { it.conversationId == selectedConversation }) { message ->
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
                                placeholder = { Text("Type your message...") },
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                                keyboardActions = KeyboardActions(
                                    onSend = { handleSendMessage() }
                                ),
                                trailingIcon = {
                                    Row {
                                        IconButton(onClick = { /* Handle attachment */ }) {
                                            Icon(Icons.Default.AccountBox, contentDescription = "Attach")
                                        }
                                    }
                                }
                            )
                            Spacer(Modifier.width(8.dp))
                            Button(
                                onClick = { handleSendMessage() },
                                modifier = Modifier.size(48.dp),
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = MaterialTheme.colors.primary,
                                    contentColor = MaterialTheme.colors.onPrimary
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
