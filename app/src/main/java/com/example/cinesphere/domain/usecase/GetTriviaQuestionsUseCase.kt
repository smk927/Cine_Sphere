package com.example.cinesphere.domain.usecase

import com.example.cinesphere.domain.model.TriviaQuestion
import com.example.cinesphere.domain.repository.TriviaRepository
import javax.inject.Inject

class GetTriviaQuestionsUseCase @Inject constructor(
    private val triviaRepository: TriviaRepository
) {
    suspend operator fun invoke(mode: String): Result<List<TriviaQuestion>> {
        val type = when (mode) {
            "TRIVIA" -> "multiple"
            "TRUE_FALSE" -> "boolean"
            else -> "multiple"
        }
        return triviaRepository.getTriviaQuestions(amount = 10, type = type)
    }
}