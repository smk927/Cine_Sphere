package com.example.cinesphere.presentation.allitems

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.model.MediaType
import com.example.cinesphere.domain.model.Movie
import com.example.cinesphere.domain.model.WebSeries
import com.example.cinesphere.domain.usecase.AddToWishlistUseCase
import com.example.cinesphere.domain.usecase.GetMoviesByGenreUseCase
import com.example.cinesphere.domain.usecase.GetPopularMediaUseCase
import com.example.cinesphere.domain.usecase.GetTrendingMediaUseCase
import com.example.cinesphere.domain.usecase.GetWishlistUseCase
import com.example.cinesphere.domain.usecase.RemoveFromWishlistUseCase
import com.example.cinesphere.domain.usecase.SearchMediaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

const val ALL_ITEMS_TYPE = "type"
const val ALL_ITEMS_GENRE_ID = "genreId"
const val ALL_ITEMS_GENRE_NAME = "genreName"

sealed class AllItemsType(val title: String) {
    object Trending : AllItemsType("Trending")
    object PopularMovies : AllItemsType("Popular Movies")
    object PopularWebSeries : AllItemsType("Popular Web Series")
    data class Genre(val genreName: String) : AllItemsType(genreName)
}

data class AllItemsUiState(
    val title: String = "",
    val items: List<Media> = emptyList(),
    val originalItems: List<Media> = emptyList(),
    val wishlist: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val allItemsLoaded: Boolean = false,
    val searchQuery: String = ""
)

@HiltViewModel
class AllItemsViewModel @Inject constructor(
    private val getPopularMediaUseCase: GetPopularMediaUseCase,
    private val getTrendingMediaUseCase: GetTrendingMediaUseCase,
    private val getMoviesByGenreUseCase: GetMoviesByGenreUseCase,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val removeFromWishlistUseCase: RemoveFromWishlistUseCase,
    getWishlistUseCase: GetWishlistUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AllItemsUiState())
    val uiState = _uiState.asStateFlow()

    val wishlist = getWishlistUseCase().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val type: String = savedStateHandle.get<String>(ALL_ITEMS_TYPE) ?: ""
    private val genreId: Int = savedStateHandle.get<Int>(ALL_ITEMS_GENRE_ID) ?: 0
    private val genreName: String = savedStateHandle.get<String>(ALL_ITEMS_GENRE_NAME) ?: ""

    private var currentPage = 1
    private var itemsType: AllItemsType? = null

    init {
        itemsType = when (type) {
            "trending" -> AllItemsType.Trending
            "popular_movies" -> AllItemsType.PopularMovies
            "popular_webseries" -> AllItemsType.PopularWebSeries
            "genre" -> AllItemsType.Genre(genreName)
            else -> null
        }

        _uiState.value = _uiState.value.copy(title = itemsType?.title ?: "")
        loadItems()
    }

    fun loadMoreItems() {
        if (_uiState.value.isLoading || _uiState.value.allItemsLoaded) return
        loadItems()
    }

    private fun loadItems() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val newItems = when (itemsType) {
                is AllItemsType.Trending -> getTrendingMediaUseCase().also {
                    _uiState.value = _uiState.value.copy(allItemsLoaded = true)
                }
                is AllItemsType.PopularMovies -> getPopularMediaUseCase(currentPage, MediaType.MOVIE)
                is AllItemsType.PopularWebSeries -> getPopularMediaUseCase(currentPage, MediaType.WEB_SERIES)
                is AllItemsType.Genre -> getMoviesByGenreUseCase(genreId).also {
                    _uiState.value = _uiState.value.copy(allItemsLoaded = true)
                }
                else -> emptyList()
            }

            val currentItems = _uiState.value.originalItems
            val allItems = (currentItems + newItems).distinctBy { it.id }
            _uiState.value = _uiState.value.copy(
                originalItems = allItems,
                items = allItems,
                isLoading = false,
                allItemsLoaded = newItems.isEmpty() && currentPage > 1
            )

            if (!_uiState.value.allItemsLoaded) {
                currentPage++
            }
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
                )
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