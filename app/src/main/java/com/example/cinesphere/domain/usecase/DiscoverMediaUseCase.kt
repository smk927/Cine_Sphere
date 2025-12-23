package com.example.cinesphere.domain.usecase

import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.model.MediaType
import com.example.cinesphere.domain.repository.MediaRepository
import javax.inject.Inject

class DiscoverMediaUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    suspend operator fun invoke(
        mediaType: MediaType,
        genreIds: Set<Int>,
        page: Int = 1
    ): List<Media> {
        val genreIdsString = genreIds.joinToString(",").ifEmpty { null }
        return when (mediaType) {
            MediaType.MOVIE -> mediaRepository.discoverMovies(genreIdsString, null, page)
            MediaType.WEB_SERIES -> mediaRepository.discoverTv(genreIdsString, null, page)
        }
    }
}