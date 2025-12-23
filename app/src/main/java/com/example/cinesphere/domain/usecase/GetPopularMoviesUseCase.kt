package com.example.cinesphere.domain.usecase

import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.model.MediaType
import com.example.cinesphere.domain.repository.MediaRepository
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(private val mediaRepository: MediaRepository) {

    suspend operator fun invoke(page: Int): List<Media> {
        return mediaRepository.getPopularMedia(page, MediaType.MOVIE)
    }
}
