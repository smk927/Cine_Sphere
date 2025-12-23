package com.example.cinesphere.presentation.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinesphere.domain.model.Genre
import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.model.MediaType
import com.example.cinesphere.domain.usecase.DiscoverMediaUseCase
import com.example.cinesphere.domain.usecase.GetMovieGenresUseCase
import com.example.cinesphere.domain.usecase.GetPopularMoviesUseCase
import com.example.cinesphere.domain.usecase.GetTvGenresUseCase
import com.example.cinesphere.domain.usecase.SearchMediaWithNaturalLanguageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DiscoverState(
    val popularMovies: List<Media> = emptyList(),
    val discoverResults: List<Media> = emptyList(),
    val searchResults: List<Media> = emptyList(),
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val isFilterDrawerOpen: Boolean = false,
    val movieGenres: List<Genre> = emptyList(),
    val tvGenres: List<Genre> = emptyList(),
    val selectedMediaType: MediaType = MediaType.MOVIE,
    val selectedGenreIds: Set<Int> = emptySet(),
    val isSearchBarActive: Boolean = false
)

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getMovieGenresUseCase: GetMovieGenresUseCase,
    private val getTvGenresUseCase: GetTvGenresUseCase,
    private val searchMediaWithNaturalLanguageUseCase: SearchMediaWithNaturalLanguageUseCase,
    private val discoverMediaUseCase: DiscoverMediaUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DiscoverState())
    val state = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        getPopularMovies()
        loadGenres()
    }

    private fun getPopularMovies() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSearching = true)
            val movies = getPopularMoviesUseCase(1)
            _state.value = _state.value.copy(popularMovies = movies, isSearching = false)
        }
    }

    private fun loadGenres() {
        viewModelScope.launch {
            val movieGenres = getMovieGenresUseCase()
            val tvGenres = getTvGenresUseCase()
            _state.value = _state.value.copy(movieGenres = movieGenres, tvGenres = tvGenres)
        }
    }

    fun onSearchQueryChange(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
        searchJob?.cancel()
        
        if (query.isNotBlank()) {
            searchJob = viewModelScope.launch {
                delay(500) // Debounce search
                processSearch(query)
            }
        } else {
            _state.value = _state.value.copy(searchResults = emptyList())
        }
    }

    private suspend fun processSearch(query: String) {
        _state.value = _state.value.copy(isSearching = true)
        val results = searchMediaWithNaturalLanguageUseCase(query)
        _state.value = _state.value.copy(searchResults = results, isSearching = false)
    }

    fun onSearchBarActiveChange(isActive: Boolean) {
        _state.value = _state.value.copy(isSearchBarActive = isActive)
    }

    fun onFilterDrawerOpen() {
        _state.value = _state.value.copy(isFilterDrawerOpen = true)
    }

    fun onFilterDrawerClose() {
        _state.value = _state.value.copy(isFilterDrawerOpen = false)
    }

    fun onMediaTypeSelected(mediaType: MediaType) {
        _state.value = _state.value.copy(selectedMediaType = mediaType, selectedGenreIds = emptySet())
        applyFilters()
    }

    fun onGenreSelected(genreId: Int) {
        val selectedIds = _state.value.selectedGenreIds.toMutableSet()
        if (selectedIds.contains(genreId)) {
            selectedIds.remove(genreId)
        } else {
            selectedIds.add(genreId)
        }
        _state.value = _state.value.copy(selectedGenreIds = selectedIds)
        applyFilters()
    }

    fun clearFilters() {
        _state.value = _state.value.copy(
            selectedMediaType = MediaType.MOVIE,
            selectedGenreIds = emptySet(),
            discoverResults = emptyList()
        )
    }

    private fun applyFilters() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSearching = true)
            val results = discoverMediaUseCase(
                _state.value.selectedMediaType,
                _state.value.selectedGenreIds
            )
            _state.value = _state.value.copy(discoverResults = results, isSearching = false)
        }
    }
}
