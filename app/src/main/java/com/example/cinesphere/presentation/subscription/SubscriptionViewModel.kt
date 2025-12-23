package com.example.cinesphere.presentation.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinesphere.domain.usecase.SubscribeUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val subscribeUserUseCase: SubscribeUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SubscriptionUiState>(SubscriptionUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun subscribe() {
        viewModelScope.launch {
            _uiState.value = SubscriptionUiState.Loading
            subscribeUserUseCase()
            _uiState.value = SubscriptionUiState.Success
        }
    }
}

sealed class SubscriptionUiState {
    object Idle : SubscriptionUiState()
    object Loading : SubscriptionUiState()
    object Success : SubscriptionUiState()
    data class Error(val message: String) : SubscriptionUiState()
}
