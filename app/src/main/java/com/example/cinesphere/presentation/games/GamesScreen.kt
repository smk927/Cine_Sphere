package com.example.cinesphere.presentation.games

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun GamesScreen(
    modifier: Modifier = Modifier,
    viewModel: GamesViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val gameState by viewModel.gameState.collectAsState()
    val gameMode by viewModel.gameMode.collectAsState()
    val currentQuestion by viewModel.currentQuestion.collectAsState()
    val score by viewModel.score.collectAsState()

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                IconButton(
                    onClick = {
                        if (gameState == GameState.Menu) {
                            onBackClick()
                        } else {
                            viewModel.goToMenu()
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (val state = gameState) {
                is GameState.Menu -> {
                    GameMenuContent(
                        onStartTrivia = { viewModel.startGame(GameMode.TRIVIA) },
                        onStartTrueFalse = { viewModel.startGame(GameMode.TRUE_FALSE) }
                    )
                }
                is GameState.Loading -> {
                    CircularProgressIndicator()
                }
                is GameState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.retryLoadTrivia() }) {
                            Text("Retry")
                        }
                    }
                }
                is GameState.Playing -> {
                    currentQuestion?.let { q ->
                        GameContent(
                            question = q,
                            gameMode = gameMode,
                            onAnswerClick = { answer -> viewModel.submitAnswer(answer) }
                        )
                    }
                }
                is GameState.GameOver -> {
                    GameOverContent(
                        score = score,
                        totalQuestions = 10,
                        onPlayAgain = { viewModel.startGame(gameMode) },
                        onBackToMenu = { viewModel.goToMenu() }
                    )
                }
            }
        }
    }
}

@Composable
fun GameMenuContent(
    onStartTrivia: () -> Unit,
    onStartTrueFalse: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Select Game Mode",
            style = MaterialTheme.typography.headlineLarge
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        MenuCard(
            title = "Classic Trivia",
            description = "Multiple Choice Questions",
            onClick = onStartTrivia
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        MenuCard(
            title = "Fact or Fiction",
            description = "True or False Speed Round",
            onClick = onStartTrueFalse
        )
    }
}

@Composable
fun MenuCard(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun GameContent(
    question: QuestionUiState,
    gameMode: GameMode,
    onAnswerClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Question ${question.questionNumber}/${question.totalQuestions}",
            style = MaterialTheme.typography.labelLarge
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = question.question.question,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        if (gameMode == GameMode.TRUE_FALSE) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { onAnswerClick("True") },
                    modifier = Modifier.weight(1f).height(80.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("TRUE")
                }
                
                Button(
                    onClick = { onAnswerClick("False") },
                    modifier = Modifier.weight(1f).height(80.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                ) {
                    Text("FALSE")
                }
            }
        } else {
            question.question.answers.forEach { answer ->
                Button(
                    onClick = { onAnswerClick(answer) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(text = answer)
                }
            }
        }
    }
}

@Composable
fun GameOverContent(
    score: Int,
    totalQuestions: Int,
    onPlayAgain: () -> Unit,
    onBackToMenu: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Game Over!",
            style = MaterialTheme.typography.headlineLarge
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "You scored $score out of $totalQuestions",
            style = MaterialTheme.typography.titleLarge
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(onClick = onPlayAgain, modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)) {
            Text("Play Again")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Button(onClick = onBackToMenu, modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)) {
            Text("Back to Menu")
        }
    }
}
