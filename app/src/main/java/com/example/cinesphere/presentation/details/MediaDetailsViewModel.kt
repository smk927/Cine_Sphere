package com.example.cinesphere.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.model.MediaType
import com.example.cinesphere.domain.usecase.GetMediaDetailsUseCase
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
class MediaDetailsViewModel @Inject constructor(
    private val getMediaDetailsUseCase: GetMediaDetailsUseCase,
    private val toggleWishlistUseCase: ToggleWishlistUseCase,
    getWishlistUseCase: GetWishlistUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<MediaDetailsUiState>(MediaDetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    val wishlist = getWishlistUseCase().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        val mediaId = savedStateHandle.get<Int>("mediaId")
        val mediaTypeString = savedStateHandle.get<String>("mediaType")

        if (mediaId == null || mediaTypeString == null) {
            _uiState.value = MediaDetailsUiState.Error("Media ID or type is missing")
        } else {
            viewModelScope.launch {
                try {
                    val mediaType = MediaType.valueOf(mediaTypeString)
                    val media = getMediaDetailsUseCase(mediaId, mediaType)
                    _uiState.value = MediaDetailsUiState.Success(media)
                } catch (e: Exception) {
                    _uiState.value = MediaDetailsUiState.Error(e.message ?: "An unknown error occurred")
                }
            }
        }
    }

    fun toggleWishlist(media: Media) {
        viewModelScope.launch {
            toggleWishlistUseCase(media)
        }
    }
}