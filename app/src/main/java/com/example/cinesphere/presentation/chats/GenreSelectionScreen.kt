package com.example.cinesphere.presentation.chats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cinesphere.domain.model.GenreRoom

@Composable
fun GenreSelectionScreen(
    onGenreClick: (String) -> Unit,
    viewModel: GenreChatViewModel = hiltViewModel()
) {
    val joinedGenres by viewModel.joinedGenres.collectAsState()
    val availableGenres by viewModel.availableGenres.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (joinedGenres.isNotEmpty()) {
            item {
                Text(
                    text = "Your Communities",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            items(joinedGenres) { genre ->
                GenreItem(
                    genre = genre,
                    onClick = { onGenreClick(genre.id) },
                    isJoined = true
                )
            }
        }

        if (availableGenres.isNotEmpty()) {
            item {
                Text(
                    text = "Explore Communities",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(availableGenres) { genre ->
                GenreItem(
                    genre = genre,
                    onClick = { viewModel.joinGenre(genre) },
                    isJoined = false
                )
            }
        }
    }
}

@Composable
fun GenreItem(
    genre: GenreRoom,
    onClick: () -> Unit,
    isJoined: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = genre.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = genre.name,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            if (!isJoined) {
                Button(onClick = onClick) {
                    Text("Join")
                }
            }
        }
    }
}