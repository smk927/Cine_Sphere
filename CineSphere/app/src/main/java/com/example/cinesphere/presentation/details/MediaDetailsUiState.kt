package com.example.cinesphere.presentation.details

import com.example.cinesphere.domain.model.Media

sealed interface MediaDetailsUiState {
    data object Loading : MediaDetailsUiState
    data class Success(val media: Media) : MediaDetailsUiState
    data class Error(val message: String) : MediaDetailsUiState
}
