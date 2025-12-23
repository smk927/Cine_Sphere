package com.example.cinesphere.presentation.details

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.model.Movie
import com.example.cinesphere.domain.model.WebSeries
import com.example.cinesphere.service.CineSpherePlaybackService

@Composable
fun MediaDetailsScreen(
    viewModel: MediaDetailsViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val wishlist by viewModel.wishlist.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (val state = uiState) {
            is MediaDetailsUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            is MediaDetailsUiState.Success -> {
                val media = state.media
                val isWishlisted = wishlist.any { it.id == media.id }
                val scrollState = rememberScrollState()

                Box(modifier = Modifier.fillMaxSize()) {
                    HeroSection(media = media)

                    Column(modifier = Modifier.verticalScroll(scrollState)) {
                        Spacer(modifier = Modifier.height(300.dp))
                        DetailsContent(media = media)
                    }

                    TopBar(media = media, isWishlisted = isWishlisted, onBackPressed = onBackPressed, onToggleWishlist = { viewModel.toggleWishlist(media) })
                }
            }
            is MediaDetailsUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground)
                    )
                }
            }
        }
    }
}

@Composable
private fun HeroSection(media: Media) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(450.dp)) {
        AsyncImage(
            model = media.posterUrl,
            contentDescription = media.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, MaterialTheme.colorScheme.background),
                        startY = 200f // Improved gradient visibility
                    )
                )
        )
    }
}

@Composable
private fun DetailsContent(media: Media) {
    Column(modifier = Modifier.padding(16.dp)) {
        media.tagline?.let {
            if (it.isNotEmpty()) {
                Text(
                    text = it,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(0f, 2f),
                            blurRadius = 4f
                        )
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        Text(
            text = media.overview,
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground)
        )
        Spacer(modifier = Modifier.height(16.dp))

        val metadataColor = MaterialTheme.colorScheme.onSurfaceVariant

        Text(
            text = "Rating: ${media.voteAverage}",
            style = MaterialTheme.typography.bodyMedium.copy(color = metadataColor)
        )
        Spacer(modifier = Modifier.height(8.dp))
        media.status?.let {
            Text(
                text = "Status: $it",
                style = MaterialTheme.typography.bodyMedium.copy(color = metadataColor)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        media.ottPlatform?.let {
            Text(
                text = "Available on: $it",
                style = MaterialTheme.typography.bodyMedium.copy(color = metadataColor)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        when (media) {
            is Movie -> {
                media.releaseDate?.let {
                    Text(
                        text = "Release Date: $it",
                        style = MaterialTheme.typography.bodyMedium.copy(color = metadataColor)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                media.runtime?.let {
                    Text(
                        text = "Runtime: $it minutes",
                        style = MaterialTheme.typography.bodyMedium.copy(color = metadataColor)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            is WebSeries -> {
                media.firstAirDate?.let {
                    Text(
                        text = "First Air Date: $it",
                        style = MaterialTheme.typography.bodyMedium.copy(color = metadataColor)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                media.lastAirDate?.let {
                    Text(
                        text = "Last Air Date: $it",
                        style = MaterialTheme.typography.bodyMedium.copy(color = metadataColor)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                media.numberOfSeasons?.let {
                    Text(
                        text = "Seasons: $it",
                        style = MaterialTheme.typography.bodyMedium.copy(color = metadataColor)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                media.numberOfEpisodes?.let {
                    Text(
                        text = "Episodes: $it",
                        style = MaterialTheme.typography.bodyMedium.copy(color = metadataColor)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    media: Media,
    isWishlisted: Boolean,
    onBackPressed: () -> Unit,
    onToggleWishlist: () -> Unit
) {
    val context = LocalContext.current
    val iconColor = Color.White

    TopAppBar(
        title = { Text("") },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = iconColor
                )
            }
        },
        actions = {
            IconButton(onClick = {
                val intent = Intent(context, CineSpherePlaybackService::class.java).apply {
                    putExtra("summary_text", media.overview)
                }
                context.startService(intent)
            }) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Read Summary",
                    tint = iconColor
                )
            }
            IconButton(onClick = onToggleWishlist) {
                Icon(
                    imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Toggle Wishlist",
                    tint = if (isWishlisted) Color.Red else iconColor // Restored Red/White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}