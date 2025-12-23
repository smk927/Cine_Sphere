package com.example.cinesphere.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cinesphere.data.session.UserSession
import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.model.Movie

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onMediaClick: (Int, String) -> Unit,
    onViewAllClick: (String, Int?, String?) -> Unit,
    onSubscriptionClick: () -> Unit,
    onGamesClick: () -> Unit,
    listState: LazyListState = rememberLazyListState()
) {
    val trendingMedia by viewModel.trendingMedia.collectAsState()
    val popularMovies by viewModel.popularMovies.collectAsState()
    val popularWebSeries by viewModel.popularWebSeries.collectAsState()
    val upcomingMovies by viewModel.upcomingMovies.collectAsState()
    val anime by viewModel.anime.collectAsState()
    val moviesByGenre by viewModel.moviesByGenre.collectAsState()
    val wishlist by viewModel.wishlist.collectAsState()
    val isSubscribed by UserSession.isSubscribed.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        state = listState // Using the hoisted state
    ) {
        item {
            TriviaCard(
                isSubscribed = isSubscribed,
                onCardClick = {
                    if (isSubscribed) {
                        onGamesClick()
                    } else {
                        onSubscriptionClick()
                    }
                }
            )
        }
        item {
            MediaSection(
                title = "Trending",
                media = trendingMedia,
                onMediaClick = onMediaClick,
                wishlist = wishlist,
                onToggleWishlist = viewModel::toggleWishlist,
                onViewAllClick = { onViewAllClick("trending", null, null) }
            )
        }
        item {
            MediaSection(
                title = "Popular Movies",
                media = popularMovies,
                onMediaClick = onMediaClick,
                wishlist = wishlist,
                onToggleWishlist = viewModel::toggleWishlist,
                onViewAllClick = { onViewAllClick("popular_movies", null, null) }
            )
        }
        item {
            MediaSection(
                title = "Popular Web Series",
                media = popularWebSeries,
                onMediaClick = onMediaClick,
                wishlist = wishlist,
                onToggleWishlist = viewModel::toggleWishlist,
                onViewAllClick = { onViewAllClick("popular_webseries", null, null) }
            )
        }
        item {
            MediaSection(
                title = "Upcoming Movies",
                media = upcomingMovies,
                onMediaClick = onMediaClick,
                wishlist = wishlist,
                onToggleWishlist = viewModel::toggleWishlist,
                onViewAllClick = { onViewAllClick("upcoming_movies", null, null) }
            )
        }
        item {
            MediaSection(
                title = "Anime",
                media = anime,
                onMediaClick = onMediaClick,
                wishlist = wishlist,
                onToggleWishlist = viewModel::toggleWishlist,
                onViewAllClick = { onViewAllClick("anime", null, null) }
            )
        }
        items(moviesByGenre.keys.toList()) { genre ->
            MediaSection(
                title = genre.name,
                media = moviesByGenre[genre].orEmpty(),
                onMediaClick = onMediaClick,
                wishlist = wishlist,
                onToggleWishlist = viewModel::toggleWishlist,
                onViewAllClick = { onViewAllClick("genre", genre.id, genre.name) }
            )
        }
    }
}

@Composable
fun TriviaCard(
    isSubscribed: Boolean,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit
) {
    // A more professional, cinematic red gradient
    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF9f0404), Color(0xFF2e0101))
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onCardClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradient)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.SportsEsports,
                    contentDescription = "Games",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val titleText = if (isSubscribed) {
                        "MOVIE TRIVIA GAMES!"
                    } else {
                        "FUN MOVIE TRIVIA"
                    }
                    val subText = if (isSubscribed) {
                        "Tap here to play!"
                    } else {
                        "Subscribe to unlock and play!"
                    }

                    Text(
                        text = titleText,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            shadow = Shadow(Color.Black.copy(alpha = 0.5f), offset = Offset(0f, 4f), blurRadius = 8f)
                        )
                    )
                    Text(
                        text = subText,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White.copy(alpha = 0.9f),
                             shadow = Shadow(Color.Black.copy(alpha = 0.5f), offset = Offset(0f, 2f), blurRadius = 4f)
                        ),
                         textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun MediaSection(
    title: String,
    media: List<Media>,
    onMediaClick: (Int, String) -> Unit,
    wishlist: List<Movie>,
    onToggleWishlist: (Media) -> Unit,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "View All",
                modifier = Modifier.clickable { onViewAllClick() },
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(media.take(10)) { mediaItem ->
                MediaCard(
                    media = mediaItem,
                    onAddToWishlist = { onToggleWishlist(mediaItem) },
                    isWishlisted = wishlist.any { it.id == mediaItem.id },
                    modifier = Modifier.clickable { onMediaClick(mediaItem.id, mediaItem.mediaType.name) }
                )
            }
        }
    }
}