package com.example.cinesphere.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinesphere.domain.model.Genre
import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.model.MediaType
import com.example.cinesphere.domain.repository.MediaRepository
import com.example.cinesphere.domain.usecase.CheckSubscriptionStatusUseCase
import com.example.cinesphere.domain.usecase.GetMoviesByGenreMapUseCase
import com.example.cinesphere.domain.usecase.GetPopularMediaUseCase
import com.example.cinesphere.domain.usecase.GetTrendingMediaUseCase
import com.example.cinesphere.domain.usecase.GetWishlistUseCase
import com.example.cinesphere.domain.usecase.ToggleWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val getPopularMediaUseCase: GetPopularMediaUseCase,
    private val getTrendingMediaUseCase: GetTrendingMediaUseCase,
    private val getMoviesByGenreMapUseCase: GetMoviesByGenreMapUseCase,
    private val toggleWishlistUseCase: ToggleWishlistUseCase,
    private val checkSubscriptionStatusUseCase: CheckSubscriptionStatusUseCase,
    getWishlistUseCase: GetWishlistUseCase
) : ViewModel() {

    private val _trendingMedia = MutableStateFlow<List<Media>>(emptyList())
    val trendingMedia = _trendingMedia.asStateFlow()

    private val _popularMovies = MutableStateFlow<List<Media>>(emptyList())
    val popularMovies = _popularMovies.asStateFlow()

    private val _popularWebSeries = MutableStateFlow<List<Media>>(emptyList())
    val popularWebSeries = _popularWebSeries.asStateFlow()

    private val _upcomingMovies = MutableStateFlow<List<Media>>(emptyList())
    val upcomingMovies = _upcomingMovies.asStateFlow()

    private val _anime = MutableStateFlow<List<Media>>(emptyList())
    val anime = _anime.asStateFlow()

    private val _moviesByGenre = MutableStateFlow<Map<Genre, List<Media>>>(emptyMap())
    val moviesByGenre = _moviesByGenre.asStateFlow()

    val wishlist = getWishlistUseCase().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadTrendingMedia()
        loadPopularMovies()
        loadPopularWebSeries()
        loadUpcomingMovies()
        loadAnime()
        loadMoviesByGenre()
        checkSubscriptionStatus()
    }

    private fun checkSubscriptionStatus() {
        viewModelScope.launch {
            checkSubscriptionStatusUseCase()
        }
    }

    private fun loadTrendingMedia() {
        viewModelScope.launch {
            _trendingMedia.value = getTrendingMediaUseCase()
        }
    }

    private fun loadPopularMovies() {
        viewModelScope.launch {
            _popularMovies.value = getPopularMediaUseCase(1, MediaType.MOVIE)
        }
    }

    private fun loadPopularWebSeries() {
        viewModelScope.launch {
            _popularWebSeries.value = getPopularMediaUseCase(1, MediaType.WEB_SERIES)
        }
    }

    private fun loadUpcomingMovies() {
        viewModelScope.launch {
            _upcomingMovies.value = mediaRepository.getUpcomingMovies(1)
        }
    }

    private fun loadAnime() {
        viewModelScope.launch {
            _anime.value = mediaRepository.getAnime(1)
        }
    }

    private fun loadMoviesByGenre() {
        viewModelScope.launch {
            _moviesByGenre.value = getMoviesByGenreMapUseCase()
        }
    }

    fun toggleWishlist(media: Media) {
        viewModelScope.launch {
            toggleWishlistUseCase(media)
        }
    }
}
