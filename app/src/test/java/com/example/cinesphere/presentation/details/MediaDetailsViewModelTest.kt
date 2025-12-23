package com.example.cinesphere.presentation.details

import androidx.lifecycle.SavedStateHandle
import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.model.MediaType
import com.example.cinesphere.domain.model.Movie
import com.example.cinesphere.domain.usecase.GetMediaDetailsUseCase
import com.example.cinesphere.domain.usecase.GetWishlistUseCase
import com.example.cinesphere.domain.usecase.ToggleWishlistUseCase
import com.example.cinesphere.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MediaDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var getMediaDetailsUseCase: GetMediaDetailsUseCase

    @Mock
    private lateinit var toggleWishlistUseCase: ToggleWishlistUseCase

    @Mock
    private lateinit var getWishlistUseCase: GetWishlistUseCase

    @Mock
    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var viewModel: MediaDetailsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        `when`(getWishlistUseCase()).thenReturn(flowOf(emptyList()))
    }

    @Test
    fun `init loads media details successfully`() = runTest {
        // Given
        val mediaId = 1
        val mediaType = MediaType.MOVIE
        val movie = Movie(id = mediaId, title = "Test Movie", overview = "Overview", posterUrl = "url", voteAverage = 8.0, mediaType = mediaType)
        
        `when`(savedStateHandle.get<Int>("mediaId")).thenReturn(mediaId)
        `when`(savedStateHandle.get<String>("mediaType")).thenReturn(mediaType.name)
        `when`(getMediaDetailsUseCase(mediaId, mediaType)).thenReturn(movie)

        // When
        viewModel = MediaDetailsViewModel(
            getMediaDetailsUseCase,
            toggleWishlistUseCase,
            getWishlistUseCase,
            savedStateHandle
        )

        // Then
        val currentState = viewModel.uiState.value
        assert(currentState is MediaDetailsUiState.Success)
        assertEquals(movie, (currentState as MediaDetailsUiState.Success).media)
    }

    @Test
    fun `init with missing args sets Error state`() {
        // Given
        `when`(savedStateHandle.get<Int>("mediaId")).thenReturn(null)
        
        // When
        viewModel = MediaDetailsViewModel(
            getMediaDetailsUseCase,
            toggleWishlistUseCase,
            getWishlistUseCase,
            savedStateHandle
        )

        // Then
        val currentState = viewModel.uiState.value
        assert(currentState is MediaDetailsUiState.Error)
        assertEquals("Media ID or type is missing", (currentState as MediaDetailsUiState.Error).message)
    }

    @Test
    fun `toggleWishlist calls toggleWishlistUseCase`() = runTest {
        // Given
        val mediaId = 1
        val mediaType = MediaType.MOVIE
        val movie = Movie(id = mediaId, title = "Test Movie", overview = "Overview", posterUrl = "url", voteAverage = 8.0, mediaType = mediaType)
        
        `when`(savedStateHandle.get<Int>("mediaId")).thenReturn(mediaId)
        `when`(savedStateHandle.get<String>("mediaType")).thenReturn(mediaType.name)
        `when`(getMediaDetailsUseCase(mediaId, mediaType)).thenReturn(movie)
        
        viewModel = MediaDetailsViewModel(
            getMediaDetailsUseCase,
            toggleWishlistUseCase,
            getWishlistUseCase,
            savedStateHandle
        )

        // When
        viewModel.toggleWishlist(movie)

        // Then
        verify(toggleWishlistUseCase).invoke(movie)
    }
}
