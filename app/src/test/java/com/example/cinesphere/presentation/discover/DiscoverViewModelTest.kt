package com.example.cinesphere.presentation.discover

import com.example.cinesphere.domain.model.Genre
import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.model.MediaType
import com.example.cinesphere.domain.model.Movie
import com.example.cinesphere.domain.usecase.*
import com.example.cinesphere.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class DiscoverViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var getPopularMoviesUseCase: GetPopularMoviesUseCase
    @Mock
    private lateinit var getMovieGenresUseCase: GetMovieGenresUseCase
    @Mock
    private lateinit var getTvGenresUseCase: GetTvGenresUseCase
    @Mock
    private lateinit var searchMediaWithNaturalLanguageUseCase: SearchMediaWithNaturalLanguageUseCase
    @Mock
    private lateinit var discoverMediaUseCase: DiscoverMediaUseCase

    private lateinit var viewModel: DiscoverViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `init loads popular movies and genres`() = runTest {
        // Given
        val movies = listOf(Movie(id = 1, title = "Popular Movie", overview = "Overview", posterUrl = "url", voteAverage = 8.0, mediaType = MediaType.MOVIE))
        val movieGenres = listOf(Genre(1, "Action"))
        val tvGenres = listOf(Genre(2, "Comedy"))

        `when`(getPopularMoviesUseCase(1)).thenReturn(movies)
        `when`(getMovieGenresUseCase()).thenReturn(movieGenres)
        `when`(getTvGenresUseCase()).thenReturn(tvGenres)

        // When
        viewModel = DiscoverViewModel(
            getPopularMoviesUseCase,
            getMovieGenresUseCase,
            getTvGenresUseCase,
            searchMediaWithNaturalLanguageUseCase,
            discoverMediaUseCase
        )

        // Advance coroutines
        advanceUntilIdle()

        // Then
        assertEquals(movies, viewModel.state.value.popularMovies)
        assertEquals(movieGenres, viewModel.state.value.movieGenres)
        assertEquals(tvGenres, viewModel.state.value.tvGenres)
    }

    @Test
    fun `onSearchQueryChange updates query`() = runTest {
        // Given
        val query = "Avengers"
        
        // Mock init calls to prevent coroutine execution issues during init
        `when`(getPopularMoviesUseCase(1)).thenReturn(emptyList())
        `when`(getMovieGenresUseCase()).thenReturn(emptyList())
        `when`(getTvGenresUseCase()).thenReturn(emptyList())

        viewModel = DiscoverViewModel(
            getPopularMoviesUseCase,
            getMovieGenresUseCase,
            getTvGenresUseCase,
            searchMediaWithNaturalLanguageUseCase,
            discoverMediaUseCase
        )

        // When
        viewModel.onSearchQueryChange(query)

        // Then
        assertEquals(query, viewModel.state.value.searchQuery)
    }
}
