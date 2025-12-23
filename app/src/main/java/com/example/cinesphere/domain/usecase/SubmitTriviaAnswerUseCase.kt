package com.example.cinesphere.domain.usecase

import com.example.cinesphere.domain.model.TriviaQuestion
import javax.inject.Inject

class SubmitTriviaAnswerUseCase @Inject constructor() {
    operator fun invoke(question: TriviaQuestion, answer: String): Boolean {
        return question.correctAnswer == answer
    }
}