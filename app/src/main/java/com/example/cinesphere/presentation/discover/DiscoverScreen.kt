package com.example.cinesphere.presentation.discover

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cinesphere.presentation.components.MediaListItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    viewModel: DiscoverViewModel = hiltViewModel(),
    onMediaClick: (Int, String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val drawerState = rememberDrawerState(initialValue = if (state.isFilterDrawerOpen) DrawerValue.Open else DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                FilterPanel(
                    state = state,
                    onMediaTypeSelected = viewModel::onMediaTypeSelected,
                    onGenreSelected = viewModel::onGenreSelected,
                    onClearFilters = viewModel::clearFilters
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                SearchBar(
                    query = state.searchQuery,
                    onQueryChange = viewModel::onSearchQueryChange,
                    onSearch = { viewModel.onSearchBarActiveChange(false) },
                    active = state.isSearchBarActive,
                    onActiveChange = { viewModel.onSearchBarActiveChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    trailingIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.FilterList, contentDescription = "Filter")
                        }
                    }
                ) {}
            }
        ) { paddingValues ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                val itemsToShow = when {
                    state.searchQuery.isNotEmpty() -> state.searchResults
                    state.discoverResults.isNotEmpty() -> state.discoverResults
                    else -> state.popularMovies
                }
                items(itemsToShow) { media ->
                    MediaListItem(media = media, onMediaClick = onMediaClick)
                }
            }
        }
    }
}