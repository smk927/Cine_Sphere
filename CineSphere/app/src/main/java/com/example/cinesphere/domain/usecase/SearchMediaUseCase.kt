package com.example.cinesphere.domain.usecase

import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.repository.MediaRepository
import javax.inject.Inject

class SearchMediaUseCase @Inject constructor(private val mediaRepository: MediaRepository) {

    suspend operator fun invoke(query: String, page: Int): List<Media> {
        if (query.isBlank()) {
            return emptyList()
        }
        return mediaRepository.search(query, page)
    }
}
