package com.example.cinesphere.domain.usecase

import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.repository.MediaRepository
import javax.inject.Inject

class GetTrendingMediaUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    suspend operator fun invoke(): List<Media> {
        return mediaRepository.getTrendingMedia()
    }
}