package com.example.cinesphere.presentation.home

import com.example.cinesphere.domain.model.MediaType
import com.example.cinesphere.domain.model.Movie
import com.example.cinesphere.domain.repository.MediaRepository
import com.example.cinesphere.domain.usecase.*
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
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock private lateinit var mediaRepository: MediaRepository
    @Mock private lateinit var getPopularMediaUseCase: GetPopularMediaUseCase
    @Mock private lateinit var getTrendingMediaUseCase: GetTrendingMediaUseCase
    @Mock private lateinit var getMoviesByGenreMapUseCase: GetMoviesByGenreMapUseCase
    @Mock private lateinit var toggleWishlistUseCase: ToggleWishlistUseCase
    @Mock private lateinit var checkSubscriptionStatusUseCase: CheckSubscriptionStatusUseCase
    @Mock private lateinit var getWishlistUseCase: GetWishlistUseCase

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        `when`(getWishlistUseCase()).thenReturn(flowOf(emptyList()))
    }

    @Test
    fun `init loads trending and popular media`() = runTest {
        // Given
        val mediaList = listOf(Movie(id = 1, title = "Test", overview = "", posterUrl = "", voteAverage = 0.0, mediaType = MediaType.MOVIE))
        `when`(getTrendingMediaUseCase()).thenReturn(mediaList)
        `when`(getPopularMediaUseCase(1, MediaType.MOVIE)).thenReturn(mediaList)
        `when`(getPopularMediaUseCase(1, MediaType.WEB_SERIES)).thenReturn(mediaList)

        // When
        viewModel = HomeViewModel(
            mediaRepository,
            getPopularMediaUseCase,
            getTrendingMediaUseCase,
            getMoviesByGenreMapUseCase,
            toggleWishlistUseCase,
            checkSubscriptionStatusUseCase,
            getWishlistUseCase
        )
        advanceUntilIdle()

        // Then
        assertEquals(mediaList, viewModel.trendingMedia.value)
        assertEquals(mediaList, viewModel.popularMovies.value)
        assertEquals(mediaList, viewModel.popularWebSeries.value)
    }
}
