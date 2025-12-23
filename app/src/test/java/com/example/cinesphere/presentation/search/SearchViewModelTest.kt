package com.example.cinesphere.presentation.search

import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.usecase.AddToWishlistUseCase
import com.example.cinesphere.domain.usecase.GetWishlistUseCase
import com.example.cinesphere.domain.usecase.RemoveFromWishlistUseCase
import com.example.cinesphere.domain.usecase.SearchMediaUseCase
import com.example.cinesphere.domain.usecase.ToggleWishlistUseCase
import com.example.cinesphere.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock private lateinit var searchMediaUseCase: SearchMediaUseCase
    @Mock private lateinit var toggleWishlistUseCase: ToggleWishlistUseCase
    @Mock private lateinit var getWishlistUseCase: GetWishlistUseCase

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        `when`(getWishlistUseCase()).thenReturn(flowOf(emptyList()))
        viewModel = SearchViewModel(
            searchMediaUseCase, toggleWishlistUseCase, getWishlistUseCase
        )
    }

    @Test
    fun `onSearchQueryChange updates result after debounce`() = runTest {
        // Given
        val query = "test"
        val results = emptyList<Media>()
        `when`(searchMediaUseCase(query, 1)).thenReturn(results)

        // When
        viewModel.onSearchQueryChange(query)
        advanceUntilIdle() // Wait for debounce

        // Then
        assertEquals(query, viewModel.searchQuery.value)
        val uiState = viewModel.uiState.value
        if (uiState is SearchUiState.Success) {
            assertEquals(results, uiState.media)
        } else {
             // If result is empty, it might be SearchUiState.Empty depending on implementation
             // In ViewModel: _uiState.value = if (result.isEmpty()) SearchUiState.Empty else SearchUiState.Success(result)
             assert(uiState is SearchUiState.Empty)
        }
    }
}
