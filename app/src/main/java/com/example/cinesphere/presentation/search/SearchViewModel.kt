package com.example.cinesphere.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.usecase.GetWishlistUseCase
import com.example.cinesphere.domain.usecase.SearchMediaUseCase
import com.example.cinesphere.domain.usecase.ToggleWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SearchUiState {
    object Loading : SearchUiState
    data class Success(val media: List<Media>) : SearchUiState
    object Empty : SearchUiState
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMediaUseCase: SearchMediaUseCase,
    private val toggleWishlistUseCase: ToggleWishlistUseCase,
    getWishlistUseCase: GetWishlistUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Empty)
    val uiState = _uiState.asStateFlow()

    val wishlist = getWishlistUseCase().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var searchJob: Job? = null

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500) // Debounce
            if (query.isNotBlank()) {
                _uiState.value = SearchUiState.Loading
                try {
                    val result = searchMediaUseCase(query, 1)
                    _uiState.value = if (result.isEmpty()) SearchUiState.Empty else SearchUiState.Success(result)
                } catch (e: Exception) {
                    _uiState.value = SearchUiState.Empty
                }
            } else {
                _uiState.value = SearchUiState.Empty
            }
        }
    }

    fun toggleWishlist(media: Media) {
        viewModelScope.launch {
            toggleWishlistUseCase(media)
        }
    }
}