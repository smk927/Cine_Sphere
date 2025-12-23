package com.example.cinesphere.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.model.Movie

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onMediaClick: (Int, String) -> Unit,
    onViewAllClick: (String, Int?, String?) -> Unit
) {
    val trendingMedia by viewModel.trendingMedia.collectAsState()
    val popularMovies by viewModel.popularMovies.collectAsState()
    val popularWebSeries by viewModel.popularWebSeries.collectAsState()
    val moviesByGenre by viewModel.moviesByGenre.collectAsState()
    val wishlist by viewModel.wishlist.collectAsState()

    LazyColumn(modifier = modifier.fillMaxSize()) {
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
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "View All",
                modifier = Modifier.clickable { onViewAllClick() }
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