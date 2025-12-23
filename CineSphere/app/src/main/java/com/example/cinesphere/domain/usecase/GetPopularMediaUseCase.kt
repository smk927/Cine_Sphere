package com.example.cinesphere.domain.usecase

import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.model.MediaType
import com.example.cinesphere.domain.repository.MediaRepository
import javax.inject.Inject

class GetPopularMediaUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    suspend operator fun invoke(page: Int, mediaType: MediaType): List<Media> {
        return mediaRepository.getPopularMedia(page, mediaType)
    }
}