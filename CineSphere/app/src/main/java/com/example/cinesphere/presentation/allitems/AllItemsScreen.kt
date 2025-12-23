package com.example.cinesphere.presentation.allitems

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.presentation.home.MediaCard

@Composable
fun AllItemsScreen(
    viewModel: AllItemsViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
    onMediaClick: (Int, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val wishlist by viewModel.wishlist.collectAsState()

    AllItemsScreen(
        uiState = uiState.copy(wishlist = wishlist),
        onBackPressed = onBackPressed,
        onMediaClick = onMediaClick,
        onToggleWishlist = viewModel::toggleWishlist,
        onLoadMore = viewModel::loadMoreItems
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllItemsScreen(
    uiState: AllItemsUiState,
    onBackPressed: () -> Unit,
    onMediaClick: (Int, String) -> Unit,
    onToggleWishlist: (Media) -> Unit,
    onLoadMore: () -> Unit
) {
    val gridState = rememberLazyGridState()

    val isAtBottom by remember {
        derivedStateOf {
            val layoutInfo = gridState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleItemIndex >= totalItems - 5
        }
    }

    LaunchedEffect(isAtBottom) {
        if (isAtBottom && !uiState.isLoading && !uiState.allItemsLoaded) {
            onLoadMore()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = uiState.title) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(uiState.items) { media ->
                MediaCard(
                    media = media,
                    onAddToWishlist = { onToggleWishlist(media) },
                    isWishlisted = uiState.wishlist.any { it.id == media.id },
                    modifier = Modifier.clickable { onMediaClick(media.id, media.mediaType.name) }
                )
            }
            if (uiState.isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}