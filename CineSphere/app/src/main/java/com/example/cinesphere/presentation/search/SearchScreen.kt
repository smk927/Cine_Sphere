
package com.example.cinesphere.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cinesphere.presentation.home.MediaCard

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onMediaClick: (Int, String) -> Unit
) {
    val searchResult by viewModel.searchResult.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val wishlist by viewModel.wishlist.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = searchQuery,
            onValueChange = viewModel::onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search for movies or web series") }
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = Modifier.padding(top = 16.dp).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(searchResult) { media ->
                MediaCard(
                    media = media,
                    onAddToWishlist = { viewModel.toggleWishlist(media) },
                    isWishlisted = wishlist.any { it.id == media.id },
                    modifier = Modifier.clickable { onMediaClick(media.id, media.mediaType.name) }
                )
            }
        }
    }
}
