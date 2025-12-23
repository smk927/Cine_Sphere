package com.example.cinesphere.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.ui.graphics.vector.ImageVector

data class GenreRoom(
    val id: String = "",
    val name: String = "",
    val icon: ImageVector = Icons.Default.Chat // Default icon if none provided
)