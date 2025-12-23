package com.example.cinesphere.presentation.allitems

import androidx.lifecycle.SavedStateHandle
import com.example.cinesphere.domain.model.MediaType
import com.example.cinesphere.domain.model.Movie
import com.example.cinesphere.domain.usecase.GetAllItemsUseCase
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
class AllItemsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var getAllItemsUseCase: GetAllItemsUseCase

    @Mock
    private lateinit var toggleWishlistUseCase: ToggleWishlistUseCase

    @Mock
    private lateinit var getWishlistUseCase: GetWishlistUseCase

    @Mock
    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var viewModel: AllItemsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        `when`(getWishlistUseCase()).thenReturn(flowOf(emptyList()))
    }

    @Test
    fun `init with Trending type loads trending media`() = runTest {
        // Given
        val trendingMovies = listOf(
            Movie(id = 1, title = "Trending Movie 1", overview = "Overview", posterUrl = "url", voteAverage = 8.0, mediaType = MediaType.MOVIE)
        )
        `when`(savedStateHandle.get<String>(ALL_ITEMS_TYPE)).thenReturn("trending")
        `when`(getAllItemsUseCase("trending", 1, null)).thenReturn(trendingMovies)

        // When
        viewModel = AllItemsViewModel(
            getAllItemsUseCase,
            toggleWishlistUseCase,
            getWishlistUseCase,
            savedStateHandle
        )

        // Then
        assertEquals(AllItemsType.Trending.title, viewModel.uiState.value.title)
        assertEquals(trendingMovies, viewModel.uiState.value.items)
    }

    @Test
    fun `toggleWishlist calls toggleWishlistUseCase`() = runTest {
        // Given
        val movie = Movie(id = 1, title = "Movie 1", overview = "Overview", posterUrl = "url", voteAverage = 8.0, mediaType = MediaType.MOVIE)
        `when`(savedStateHandle.get<String>(ALL_ITEMS_TYPE)).thenReturn("trending")
        `when`(getAllItemsUseCase("trending", 1, null)).thenReturn(listOf(movie))
        
        viewModel = AllItemsViewModel(
            getAllItemsUseCase,
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
