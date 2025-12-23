package com.example.cinesphere.presentation.auth

import com.example.cinesphere.domain.usecase.SignInUseCase
import com.example.cinesphere.domain.usecase.SignUpUseCase
import com.example.cinesphere.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class AuthViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var signInUseCase: SignInUseCase

    @Mock
    private lateinit var signUpUseCase: SignUpUseCase

    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = AuthViewModel(signInUseCase, signUpUseCase)
    }

    @Test
    fun `signIn success updates state to Success`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password"
        `when`(signInUseCase(email, password)).thenReturn(Result.success(Unit))

        // When
        viewModel.signIn(email, password)

        // Then
        assertEquals(AuthState.Success, viewModel.authState.value)
    }

    @Test
    fun `signIn failure updates state to Error`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "wrong_password"
        val errorMessage = "Login failed"
        `when`(signInUseCase(email, password)).thenReturn(Result.failure(Exception(errorMessage)))

        // When
        viewModel.signIn(email, password)

        // Then
        val currentState = viewModel.authState.value
        assert(currentState is AuthState.Error)
        assertEquals(errorMessage, (currentState as AuthState.Error).message)
    }

    @Test
    fun `signUp success updates state to Success`() = runTest {
        // Given
        val email = "new@example.com"
        val password = "password"
        `when`(signUpUseCase(email, password)).thenReturn(Result.success(Unit))

        // When
        viewModel.signUp(email, password)

        // Then
        assertEquals(AuthState.Success, viewModel.authState.value)
    }

    @Test
    fun `signUp failure updates state to Error`() = runTest {
        // Given
        val email = "existing@example.com"
        val password = "password"
        val errorMessage = "User already exists"
        `when`(signUpUseCase(email, password)).thenReturn(Result.failure(Exception(errorMessage)))

        // When
        viewModel.signUp(email, password)

        // Then
        val currentState = viewModel.authState.value
        assert(currentState is AuthState.Error)
        assertEquals(errorMessage, (currentState as AuthState.Error).message)
    }

    @Test
    fun `resetState sets state to Idle`() {
        // When
        viewModel.resetState()

        // Then
        assertEquals(AuthState.Idle, viewModel.authState.value)
    }
}
