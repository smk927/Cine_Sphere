package com.example.cinesphere.presentation.subscription

import com.example.cinesphere.domain.usecase.SubscribeUserUseCase
import com.example.cinesphere.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class SubscriptionViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var subscribeUserUseCase: SubscribeUserUseCase

    private lateinit var viewModel: SubscriptionViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = SubscriptionViewModel(subscribeUserUseCase)
    }

    @Test
    fun `subscribe updates state to Success`() = runTest {
        // When
        viewModel.subscribe()

        // Then
        assertEquals(SubscriptionUiState.Success, viewModel.uiState.value)
    }
}
