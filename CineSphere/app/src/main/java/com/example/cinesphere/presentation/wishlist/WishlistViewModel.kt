package com.example.cinesphere.presentation.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinesphere.domain.model.Movie
import com.example.cinesphere.domain.usecase.GetWishlistUseCase
import com.example.cinesphere.domain.usecase.RemoveFromWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    getWishlistUseCase: GetWishlistUseCase,
    private val removeFromWishlistUseCase: RemoveFromWishlistUseCase
) : ViewModel() {

    val wishlist: StateFlow<List<Movie>> = getWishlistUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun removeFromWishlist(movie: Movie) {
        viewModelScope.launch {
            removeFromWishlistUseCase(movie)
        }
    }
}