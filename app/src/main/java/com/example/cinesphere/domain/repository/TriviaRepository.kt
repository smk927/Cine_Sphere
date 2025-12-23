package com.example.cinesphere.domain.repository

import com.example.cinesphere.domain.model.TriviaQuestion

interface TriviaRepository {
    suspend fun getTriviaQuestions(amount: Int = 10, type: String = "multiple"): Result<List<TriviaQuestion>>
}