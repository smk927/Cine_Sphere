package com.example.cinesphere.presentation.wishlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cinesphere.presentation.home.MediaCard

@Composable
fun WishlistScreen(
    modifier: Modifier = Modifier,
    viewModel: WishlistViewModel = hiltViewModel(),
    onMediaClick: (Int, String) -> Unit
) {
    val wishlist by viewModel.wishlist.collectAsState()
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(wishlist) { movie ->
            MediaCard(
                media = movie,
                onAddToWishlist = { viewModel.removeFromWishlist(movie) },
                isWishlisted = true,
                modifier = Modifier.clickable { onMediaClick(movie.id, movie.mediaType.name) }
            )
        }
    }
}