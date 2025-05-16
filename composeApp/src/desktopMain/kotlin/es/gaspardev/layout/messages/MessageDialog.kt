package es.gaspardev.layout.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.gaspardev.components.UserAvatar
import es.gaspardev.layout.FloatingDialog
import es.gaspardev.pages.sampleUser

@Composable
fun MessageDialog(
    onDismiss: () -> Unit,
    onSend: (String, String) -> Unit
) {
    FloatingDialog(onDismiss = onDismiss) {
        var searchQuery by remember { mutableStateOf("") }
        var selectedAthlete by remember { mutableStateOf<String?>(null) }
        var messageText by remember { mutableStateOf("") }


        Surface(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "New Message",
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Send a message to one of your athletes.",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search athletes...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    },
                    singleLine = true
                )

                LazyColumn(
                    modifier = Modifier
                        .height(200.dp)
                        .padding(vertical = 8.dp)
                        .border(1.dp, MaterialTheme.colors.primary, RoundedCornerShape(4.dp))
                ) {
                    val filteredAthletes = sampleUser.filter {
                        it.name.contains(searchQuery, ignoreCase = true) ||
                                it.email.contains(searchQuery, ignoreCase = true)
                    }

                    if (filteredAthletes.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No athletes found")
                            }
                        }
                    } else {
                        items(filteredAthletes) { athlete ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedAthlete = athlete.email }
                                    .background(
                                        if (selectedAthlete == athlete.email)
                                            MaterialTheme.colors.primary.copy(alpha = 0.1f)
                                        else Color.Transparent
                                    )
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                UserAvatar(sampleUser[0])
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = athlete.name,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = athlete.email,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    placeholder = { Text("Type your message here...") },
                    minLines = 5,
                    maxLines = 5
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.surface,
                            contentColor = MaterialTheme.colors.primary
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            if (selectedAthlete != null && messageText.isNotBlank()) {
                                onSend(selectedAthlete!!, messageText)
                            }
                        },
                        enabled = selectedAthlete != null && messageText.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = MaterialTheme.colors.onPrimary
                        )
                    ) {
                        Text("Send Message")
                    }
                }
            }
        }
    }

}