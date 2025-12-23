package com.example.cinesphere.domain.model

import android.text.Html

data class TriviaQuestion(
    val question: String,
    val answers: List<String>,
    val correctAnswer: String
) {
    companion object {
        fun fromDto(dto: com.example.cinesphere.data.dto.TriviaQuestionDto): TriviaQuestion {
            val correctAnswer = Html.fromHtml(dto.correctAnswer, Html.FROM_HTML_MODE_LEGACY).toString()
            val incorrectAnswers = dto.incorrectAnswers.map { Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY).toString() }
            val allAnswers = (incorrectAnswers + correctAnswer).shuffled()
            return TriviaQuestion(
                question = Html.fromHtml(dto.question, Html.FROM_HTML_MODE_LEGACY).toString(),
                answers = allAnswers,
                correctAnswer = correctAnswer
            )
        }
    }
}