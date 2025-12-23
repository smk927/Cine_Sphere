package com.example.cinesphere.domain.usecase

import com.example.cinesphere.domain.model.Genre
import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.model.genres
import javax.inject.Inject

class GetMoviesByGenreMapUseCase @Inject constructor(
    private val getMoviesByGenreUseCase: GetMoviesByGenreUseCase
) {
    suspend operator fun invoke(): Map<Genre, List<Media>> {
        val genreMap = mutableMapOf<Genre, List<Media>>()
        for (genre in genres) {
            val movies = getMoviesByGenreUseCase(genre.id)
            if (movies.isNotEmpty()) {
                genreMap[genre] = movies
            }
        }
        return genreMap
    }
}