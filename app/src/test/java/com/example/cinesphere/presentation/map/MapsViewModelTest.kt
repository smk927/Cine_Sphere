package com.example.cinesphere.presentation.map

import com.example.cinesphere.domain.model.Cinema
import com.example.cinesphere.domain.usecase.FetchNearbyCinemasUseCase
import com.example.cinesphere.util.MainDispatcherRule
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class MapsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var fetchNearbyCinemasUseCase: FetchNearbyCinemasUseCase
    
    @Mock
    private lateinit var mockLatLng: LatLng

    private lateinit var viewModel: MapsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = MapsViewModel(fetchNearbyCinemasUseCase)
    }

    @Test
    fun `fetchLocationAndCinemas updates state`() = runTest {
        // Given
        val cinemaLocation = LatLng(10.0, 10.0)
        val cinemas = listOf(Cinema("1", "Cinema 1", cinemaLocation, "Vicinity"))
        `when`(fetchNearbyCinemasUseCase(10000)).thenReturn(Pair(mockLatLng, cinemas))

        // When
        viewModel.fetchLocationAndCinemas()
        advanceUntilIdle()

        // Then
        assertEquals(mockLatLng, viewModel.userLocation.value)
        assertEquals(cinemas, viewModel.cinemas.value)
    }
}
