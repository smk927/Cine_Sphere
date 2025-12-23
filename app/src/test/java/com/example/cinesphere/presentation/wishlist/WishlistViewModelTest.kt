package com.example.cinesphere.presentation.wishlist

import com.example.cinesphere.domain.model.MediaType
import com.example.cinesphere.domain.model.Movie
import com.example.cinesphere.domain.usecase.GetWishlistUseCase
import com.example.cinesphere.domain.usecase.RemoveFromWishlistUseCase
import com.example.cinesphere.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
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
class WishlistViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var getWishlistUseCase: GetWishlistUseCase
    @Mock
    private lateinit var removeFromWishlistUseCase: RemoveFromWishlistUseCase

    private lateinit var viewModel: WishlistViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `wishlist emits items from use case`() = runTest {
        // Given
        val movies = listOf(Movie(id = 1, title = "Movie", overview = "", posterUrl = "", voteAverage = 0.0, mediaType = MediaType.MOVIE))
        `when`(getWishlistUseCase()).thenReturn(flowOf(movies))

        // When
        viewModel = WishlistViewModel(getWishlistUseCase, removeFromWishlistUseCase)
        
        // Start collecting to trigger WhileSubscribed
        val job = launch {
            viewModel.wishlist.collect()
        }
        advanceUntilIdle()

        // Then
        assertEquals(movies, viewModel.wishlist.value)
        job.cancel()
    }

    @Test
    fun `removeFromWishlist calls use case`() = runTest {
        // Given
        `when`(getWishlistUseCase()).thenReturn(flowOf(emptyList()))
        viewModel = WishlistViewModel(getWishlistUseCase, removeFromWishlistUseCase)
        val movie = Movie(id = 1, title = "Movie", overview = "", posterUrl = "", voteAverage = 0.0, mediaType = MediaType.MOVIE)

        // When
        viewModel.removeFromWishlist(movie)
        advanceUntilIdle()

        // Then
        verify(removeFromWishlistUseCase).invoke(movie)
    }
}
