package com.example.cinesphere.presentation.games

import com.example.cinesphere.domain.model.TriviaQuestion
import com.example.cinesphere.domain.usecase.GetTriviaQuestionsUseCase
import com.example.cinesphere.domain.usecase.SubmitTriviaAnswerUseCase
import com.example.cinesphere.util.MainDispatcherRule
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
class GamesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var getTriviaQuestionsUseCase: GetTriviaQuestionsUseCase
    @Mock
    private lateinit var submitTriviaAnswerUseCase: SubmitTriviaAnswerUseCase

    private lateinit var viewModel: GamesViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = GamesViewModel(getTriviaQuestionsUseCase, submitTriviaAnswerUseCase)
    }

    @Test
    fun `startGame loads trivia and updates state to Playing`() = runTest {
        // Given
        val questions = listOf(TriviaQuestion("Q1", listOf("A", "B"), "A"))
        `when`(getTriviaQuestionsUseCase("TRIVIA")).thenReturn(Result.success(questions))

        // When
        viewModel.startGame(GameMode.TRIVIA)
        advanceUntilIdle()

        // Then
        assertEquals(GameMode.TRIVIA, viewModel.gameMode.value)
        assertEquals(GameState.Playing, viewModel.gameState.value)
        assertEquals(questions[0], viewModel.currentQuestion.value?.question)
    }

    @Test
    fun `submitAnswer updates score on correct answer`() = runTest {
        // Given
        val question = TriviaQuestion("Q1", listOf("A", "B"), "A")
        val questions = listOf(question)
        `when`(getTriviaQuestionsUseCase("TRIVIA")).thenReturn(Result.success(questions))
        `when`(submitTriviaAnswerUseCase(question, "A")).thenReturn(true)
        
        viewModel.startGame(GameMode.TRIVIA)
        advanceUntilIdle() // Ensure game starts

        // When
        viewModel.submitAnswer("A")

        // Then
        assertEquals(1, viewModel.score.value)
    }
}
