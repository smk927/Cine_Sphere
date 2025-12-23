package com.example.cinesphere.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinesphere.domain.model.Genre
import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.model.MediaType
import com.example.cinesphere.domain.model.Movie
import com.example.cinesphere.domain.model.genres
import com.example.cinesphere.domain.usecase.AddToWishlistUseCase
import com.example.cinesphere.domain.usecase.GetMoviesByGenreUseCase
import com.example.cinesphere.domain.usecase.GetPopularMediaUseCase
import com.example.cinesphere.domain.usecase.GetTrendingMediaUseCase
import com.example.cinesphere.domain.usecase.GetWishlistUseCase
import com.example.cinesphere.domain.usecase.RemoveFromWishlistUseCase
import com.example.cinesphere.domain.model.WebSeries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularMediaUseCase: GetPopularMediaUseCase,
    private val getTrendingMediaUseCase: GetTrendingMediaUseCase,
    private val getMoviesByGenreUseCase: GetMoviesByGenreUseCase,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val removeFromWishlistUseCase: RemoveFromWishlistUseCase,
    getWishlistUseCase: GetWishlistUseCase
) : ViewModel() {

    private val _trendingMedia = MutableStateFlow<List<Media>>(emptyList())
    val trendingMedia = _trendingMedia.asStateFlow()

    private val _popularMovies = MutableStateFlow<List<Media>>(emptyList())
    val popularMovies = _popularMovies.asStateFlow()

    private val _popularWebSeries = MutableStateFlow<List<Media>>(emptyList())
    val popularWebSeries = _popularWebSeries.asStateFlow()

    private val _moviesByGenre = MutableStateFlow<Map<Genre, List<Media>>>(emptyMap())
    val moviesByGenre = _moviesByGenre.asStateFlow()

    val wishlist = getWishlistUseCase().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadTrendingMedia()
        loadPopularMovies()
        loadPopularWebSeries()
        loadMoviesByGenre()
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

    private fun loadMoviesByGenre() {
        viewModelScope.launch {
            val genreMap = mutableMapOf<Genre, List<Media>>()
            for (genre in genres) {
                val movies = getMoviesByGenreUseCase(genre.id)
                if (movies.isNotEmpty()) {
                    genreMap[genre] = movies
                }
            }
            _moviesByGenre.value = genreMap
        }
    }

    fun toggleWishlist(media: Media) {
        viewModelScope.launch {
            val movie = when (media) {
                is Movie -> media
                is WebSeries -> Movie(
                    id = media.id,
                    title = media.title,
                    overview = media.overview,
                    posterUrl = media.posterUrl,
                    voteAverage = media.voteAverage,
                    mediaType = MediaType.WEB_SERIES
                ).apply { this.genres = media.genres }
                else -> return@launch
            }

            if (wishlist.value.any { it.id == movie.id }) {
                removeFromWishlistUseCase(movie)
            } else {
                addToWishlistUseCase(movie)
            }
        }
    }
}