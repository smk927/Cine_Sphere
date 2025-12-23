package com.example.cinesphere.data.repository

import com.example.cinesphere.data.remote.TriviaApi
import com.example.cinesphere.domain.model.TriviaQuestion
import com.example.cinesphere.domain.repository.TriviaRepository
import javax.inject.Inject

class TriviaRepositoryImpl @Inject constructor(
    private val api: TriviaApi
) : TriviaRepository {
    override suspend fun getTriviaQuestions(amount: Int, type: String): Result<List<TriviaQuestion>> {
        return try {
            val response = api.getMovieTrivia(amount = amount, type = type)
            if (response.responseCode == 0) {
                val questions = response.results.map { TriviaQuestion.fromDto(it) }
                Result.success(questions)
            } else {
                Result.failure(Exception("Failed to load questions. Code: ${response.responseCode}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}