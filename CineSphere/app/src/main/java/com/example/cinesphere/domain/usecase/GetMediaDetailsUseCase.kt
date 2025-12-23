package com.example.cinesphere.domain.usecase

import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.model.MediaType
import com.example.cinesphere.domain.repository.MediaRepository
import javax.inject.Inject

class GetMediaDetailsUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    suspend operator fun invoke(mediaId: Int, mediaType: MediaType): Media {
        return mediaRepository.getMediaDetails(mediaId, mediaType)
    }
}