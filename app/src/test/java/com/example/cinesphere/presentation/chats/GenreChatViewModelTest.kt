package com.example.cinesphere.presentation.chats

import com.example.cinesphere.domain.model.GenreRoom
import com.example.cinesphere.domain.usecase.*
import com.example.cinesphere.util.MainDispatcherRule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class GenreChatViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var getChatGenresUseCase: GetChatGenresUseCase
    @Mock
    private lateinit var joinGenreUseCase: JoinGenreUseCase
    @Mock
    private lateinit var leaveGenreUseCase: LeaveGenreUseCase
    @Mock
    private lateinit var getChatMessagesUseCase: GetChatMessagesUseCase
    @Mock
    private lateinit var sendMessageUseCase: SendMessageUseCase
    @Mock
    private lateinit var deleteMessageUseCase: DeleteMessageUseCase
    @Mock
    private lateinit var getGenreRoomUseCase: GetGenreRoomUseCase
    @Mock
    private lateinit var auth: FirebaseAuth
    @Mock
    private lateinit var firebaseUser: FirebaseUser

    private lateinit var viewModel: GenreChatViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        `when`(auth.currentUser).thenReturn(firebaseUser)
        `when`(firebaseUser.uid).thenReturn("test_user_id")
    }

    @Test
    fun `loadGenres updates joined and available genres`() = runTest {
        // Given
        val joinedGenres = listOf(GenreRoom("1", "Action"))
        val availableGenres = listOf(GenreRoom("2", "Comedy"))
        `when`(getChatGenresUseCase()).thenReturn(Pair(joinedGenres, availableGenres))

        viewModel = GenreChatViewModel(
            getChatGenresUseCase, joinGenreUseCase, leaveGenreUseCase,
            getChatMessagesUseCase, sendMessageUseCase, deleteMessageUseCase,
            getGenreRoomUseCase, auth
        )

        // Then
        assertEquals(joinedGenres, viewModel.joinedGenres.value)
        assertEquals(availableGenres, viewModel.availableGenres.value)
    }

    @Test
    fun `sendMessage calls sendMessageUseCase`() = runTest {
        // Given
        val genreId = "1"
        val messageText = "Hello!"
        `when`(getChatGenresUseCase()).thenReturn(Pair(emptyList(), emptyList())) // For init

        viewModel = GenreChatViewModel(
            getChatGenresUseCase, joinGenreUseCase, leaveGenreUseCase,
            getChatMessagesUseCase, sendMessageUseCase, deleteMessageUseCase,
            getGenreRoomUseCase, auth
        )

        // When
        viewModel.sendMessage(genreId, messageText)

        // Then
        verify(sendMessageUseCase).invoke(genreId, messageText)
    }
}
