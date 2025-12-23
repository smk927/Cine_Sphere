package com.example.cinesphere.data.remote

import com.example.cinesphere.data.dto.TriviaResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaApi {
    @GET("api.php")
    suspend fun getMovieTrivia(
        @Query("amount") amount: Int = 10,
        @Query("category") category: Int = 11, // Movie category
        @Query("type") type: String // "multiple" or "boolean"
    ): TriviaResponseDto
}
