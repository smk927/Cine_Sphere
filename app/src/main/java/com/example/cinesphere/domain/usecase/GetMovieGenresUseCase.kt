package com.example.cinesphere.domain.usecase

import com.example.cinesphere.domain.model.Genre
import com.example.cinesphere.domain.repository.MediaRepository
import javax.inject.Inject

class GetMovieGenresUseCase @Inject constructor(private val mediaRepository: MediaRepository) {
    suspend operator fun invoke(): List<Genre> {
        return mediaRepository.getMovieGenres()
    }
}
