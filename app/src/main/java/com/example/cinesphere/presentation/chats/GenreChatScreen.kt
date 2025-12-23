package com.example.cinesphere.presentation.chats

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cinesphere.domain.model.DiscussionMessage
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun GenreChatScreen(
    genreId: String,
    onBackClick: () -> Unit,
    viewModel: GenreChatViewModel = hiltViewModel()
) {
    LaunchedEffect(genreId) {
        viewModel.loadMessages(genreId)
    }

    val messages by viewModel.messages.collectAsState()
    val currentGenre by viewModel.currentGenre.collectAsState()
    val currentUserId = viewModel.currentUserId
    val leaveGroupState by viewModel.leaveGroupState.collectAsState()
    val context = LocalContext.current

    var showMenu by remember { mutableStateOf(false) }

    LaunchedEffect(leaveGroupState) {
        when (leaveGroupState) {
            is LeaveGroupState.Success -> {
                Toast.makeText(context, "You have left the group.", Toast.LENGTH_SHORT).show()
                viewModel.resetLeaveGroupState()
                onBackClick()
            }
            is LeaveGroupState.Error -> {
                Toast.makeText(context, (leaveGroupState as LeaveGroupState.Error).message, Toast.LENGTH_SHORT).show()
                viewModel.resetLeaveGroupState()
            }
            else -> {}
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Surface(
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(4.dp)
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                
                Text(
                    text = currentGenre?.name ?: "Chat",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                )

                Box {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options"
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Leave Group") },
                            onClick = {
                                viewModel.leaveGroup(genreId)
                                showMenu = false
                            }
                        )
                    }
                }
            }
        }

        // Messages List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            reverseLayout = false, 
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { message ->
                val isMyMessage = message.senderId == currentUserId
                MessageItem(
                    message = message, 
                    isMyMessage = isMyMessage,
                    onDelete = { 
                        viewModel.deleteMessage(genreId, message.id)
                    }
                )
            }
        }

        // Input Area
        MessageInput(onSendMessage = { text ->
            viewModel.sendMessage(genreId, text)
        })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageItem(
    message: DiscussionMessage, 
    isMyMessage: Boolean,
    onDelete: () -> Unit
) {
    var showDeleteMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isMyMessage) Alignment.End else Alignment.Start
    ) {
        if (!isMyMessage) {
            Text(
                text = message.senderName,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
            )
        }
        
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isMyMessage) 16.dp else 4.dp,
                        bottomEnd = if (isMyMessage) 4.dp else 16.dp
                    )
                )
                .background(
                    if (isMyMessage) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceVariant
                )
                .combinedClickable(
                    onClick = {},
                    onLongClick = {
                        if (isMyMessage) {
                            showDeleteMenu = true
                        }
                    }
                )
                .padding(12.dp)
        ) {
            Text(
                text = message.text,
                color = if (isMyMessage) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurfaceVariant
            )

            DropdownMenu(
                expanded = showDeleteMenu,
                onDismissRequest = { showDeleteMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = {
                        onDelete()
                        showDeleteMenu = false
                    }
                )
            }
        }
        
        Text(
            text = formatTimestamp(message.timestamp),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(top = 2.dp, start = 4.dp, end = 4.dp),
            fontSize = 10.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInput(onSendMessage: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Type a message...") },
            modifier = Modifier.weight(1f),
            maxLines = 3,
            shape = RoundedCornerShape(24.dp)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        IconButton(
            onClick = {
                if (text.isNotBlank()) {
                    onSendMessage(text)
                    text = ""
                }
            },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary, shape = androidx.compose.foundation.shape.CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
