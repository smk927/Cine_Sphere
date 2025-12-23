package com.example.cinesphere.presentation.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinesphere.domain.model.TriviaQuestion
import com.example.cinesphere.domain.usecase.GetTriviaQuestionsUseCase
import com.example.cinesphere.domain.usecase.SubmitTriviaAnswerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class GameMode {
    MENU,
    TRIVIA,
    TRUE_FALSE
}

sealed class GameState {
    object Menu : GameState()
    object Loading : GameState()
    object Playing : GameState()
    object GameOver : GameState()
    data class Error(val message: String) : GameState()
}

data class QuestionUiState(
    val question: TriviaQuestion,
    val questionNumber: Int,
    val totalQuestions: Int
)

@HiltViewModel
class GamesViewModel @Inject constructor(
    private val getTriviaQuestionsUseCase: GetTriviaQuestionsUseCase,
    private val submitTriviaAnswerUseCase: SubmitTriviaAnswerUseCase
) : ViewModel() {

    private val _gameState = MutableStateFlow<GameState>(GameState.Menu)
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _gameMode = MutableStateFlow(GameMode.MENU)
    val gameMode: StateFlow<GameMode> = _gameMode.asStateFlow()

    private val _currentQuestion = MutableStateFlow<QuestionUiState?>(null)
    val currentQuestion: StateFlow<QuestionUiState?> = _currentQuestion.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private var questions: List<TriviaQuestion> = emptyList()
    private var currentIndex = 0

    fun startGame(mode: GameMode) {
        _gameMode.value = mode
        loadTrivia(mode)
    }

    fun retryLoadTrivia() {
        loadTrivia(_gameMode.value)
    }

    private fun loadTrivia(mode: GameMode) {
        if (mode == GameMode.MENU) return

        viewModelScope.launch {
            _gameState.value = GameState.Loading
            _score.value = 0
            currentIndex = 0
            getTriviaQuestionsUseCase(mode.name)
                .onSuccess {
                    questions = it
                    loadNextQuestion()
                    _gameState.value = GameState.Playing
                }
                .onFailure {
                    _gameState.value = GameState.Error(it.message ?: "Unknown error")
                }
        }
    }

    private fun loadNextQuestion() {
        if (currentIndex < questions.size) {
            val q = questions[currentIndex]
            _currentQuestion.value = QuestionUiState(
                question = q,
                questionNumber = currentIndex + 1,
                totalQuestions = questions.size
            )
        } else {
            _gameState.value = GameState.GameOver
        }
    }

    fun submitAnswer(answer: String) {
        val currentQ = _currentQuestion.value ?: return
        if (submitTriviaAnswerUseCase(currentQ.question, answer)) {
            _score.value++
        }
        currentIndex++
        loadNextQuestion()
    }

    fun goToMenu() {
        _gameState.value = GameState.Menu
        _gameMode.value = GameMode.MENU
        _score.value = 0
        questions = emptyList()
        _currentQuestion.value = null
    }
}
