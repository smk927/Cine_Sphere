package com.example.cinesphere.presentation.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinesphere.domain.model.DiscussionMessage
import com.example.cinesphere.domain.model.GenreRoom
import com.example.cinesphere.domain.usecase.DeleteMessageUseCase
import com.example.cinesphere.domain.usecase.GetChatGenresUseCase
import com.example.cinesphere.domain.usecase.GetChatMessagesUseCase
import com.example.cinesphere.domain.usecase.GetGenreRoomUseCase
import com.example.cinesphere.domain.usecase.JoinGenreUseCase
import com.example.cinesphere.domain.usecase.LeaveGenreUseCase
import com.example.cinesphere.domain.usecase.SendMessageUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LeaveGroupState {
    object Idle : LeaveGroupState()
    object Loading : LeaveGroupState()
    object Success : LeaveGroupState()
    data class Error(val message: String) : LeaveGroupState()
}

@HiltViewModel
class GenreChatViewModel @Inject constructor(
    private val getChatGenresUseCase: GetChatGenresUseCase,
    private val joinGenreUseCase: JoinGenreUseCase,
    private val leaveGenreUseCase: LeaveGenreUseCase,
    private val getChatMessagesUseCase: GetChatMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase,
    private val getGenreRoomUseCase: GetGenreRoomUseCase,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _joinedGenres = MutableStateFlow<List<GenreRoom>>(emptyList())
    val joinedGenres: StateFlow<List<GenreRoom>> = _joinedGenres.asStateFlow()

    private val _availableGenres = MutableStateFlow<List<GenreRoom>>(emptyList())
    val availableGenres: StateFlow<List<GenreRoom>> = _availableGenres.asStateFlow()

    private val _messages = MutableStateFlow<List<DiscussionMessage>>(emptyList())
    val messages: StateFlow<List<DiscussionMessage>> = _messages.asStateFlow()

    private val _currentGenre = MutableStateFlow<GenreRoom?>(null)
    val currentGenre: StateFlow<GenreRoom?> = _currentGenre.asStateFlow()

    private val _leaveGroupState = MutableStateFlow<LeaveGroupState>(LeaveGroupState.Idle)
    val leaveGroupState: StateFlow<LeaveGroupState> = _leaveGroupState.asStateFlow()

    val currentUserId: String?
        get() = auth.currentUser?.uid

    init {
        loadGenres()
    }

    fun loadGenres() {
        viewModelScope.launch {
            val (joined, available) = getChatGenresUseCase()
            _joinedGenres.value = joined
            _availableGenres.value = available
        }
    }

    fun joinGenre(genre: GenreRoom) {
        viewModelScope.launch {
            joinGenreUseCase(genre.id)
            loadGenres() // Refresh lists
        }
    }

    fun leaveGroup(genreId: String) {
        viewModelScope.launch {
            _leaveGroupState.value = LeaveGroupState.Loading
            try {
                leaveGenreUseCase(genreId)
                _leaveGroupState.value = LeaveGroupState.Success
                loadGenres()
            } catch (e: Exception) {
                _leaveGroupState.value = LeaveGroupState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }

    fun resetLeaveGroupState() {
        _leaveGroupState.value = LeaveGroupState.Idle
    }

    fun loadMessages(genreId: String) {
        _currentGenre.value = getGenreRoomUseCase(genreId)
        viewModelScope.launch {
            getChatMessagesUseCase(genreId)
                .catch { e ->
                    e.printStackTrace() // Log error but don't crash
                }
                .collect {
                    _messages.value = it
                }
        }
    }

    fun sendMessage(genreId: String, text: String) {
        viewModelScope.launch {
            sendMessageUseCase(genreId, text)
        }
    }

    fun deleteMessage(genreId: String, messageId: String) {
        viewModelScope.launch {
            deleteMessageUseCase(genreId, messageId)
        }
    }
}