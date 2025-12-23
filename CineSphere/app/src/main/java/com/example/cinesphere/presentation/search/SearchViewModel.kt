
package com.example.cinesphere.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.model.Movie
import com.example.cinesphere.domain.usecase.AddToWishlistUseCase
import com.example.cinesphere.domain.usecase.GetWishlistUseCase
import com.example.cinesphere.domain.usecase.RemoveFromWishlistUseCase
import com.example.cinesphere.domain.usecase.SearchMediaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMediaUseCase: SearchMediaUseCase,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val removeFromWishlistUseCase: RemoveFromWishlistUseCase,
    getWishlistUseCase: GetWishlistUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchResult = MutableStateFlow<List<Media>>(emptyList())
    val searchResult = _searchResult.asStateFlow()

    val wishlist = getWishlistUseCase().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var searchJob: Job? = null

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500) // Debounce
            if (query.isNotBlank()) {
                _searchResult.value = searchMediaUseCase(query, 1)
            } else {
                _searchResult.value = emptyList()
            }
        }
    }

    fun toggleWishlist(media: Media) {
        viewModelScope.launch {
            if (media is Movie) {
                if (wishlist.value.any { it.id == media.id }) {
                    removeFromWishlistUseCase(media)
                } else {
                    addToWishlistUseCase(media)
                }
            }
        }
    }
}
