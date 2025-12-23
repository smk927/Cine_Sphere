package com.example.cinesphere.domain.usecase

import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.repository.MediaRepository
import javax.inject.Inject

class GetMoviesByGenreUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    suspend operator fun invoke(genreId: Int): List<Media> {
        return mediaRepository.getMoviesByGenre(genreId)
    }
}