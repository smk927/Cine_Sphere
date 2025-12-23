package com.example.cinesphere.domain.usecase

import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.model.MediaType
import com.example.cinesphere.domain.repository.MediaRepository
import javax.inject.Inject

class GetAllItemsUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    suspend operator fun invoke(type: String, page: Int, genreId: Int? = null): List<Media> {
        return when (type) {
            "trending" -> mediaRepository.getTrendingMedia(page)
            "popular_movies" -> mediaRepository.getPopularMedia(page, MediaType.MOVIE)
            "popular_webseries" -> mediaRepository.getPopularMedia(page, MediaType.WEB_SERIES)
            "upcoming_movies" -> mediaRepository.getUpcomingMovies(page)
            "anime" -> mediaRepository.getAnime(page)
            "genre" -> mediaRepository.discoverMovies(genreId.toString(), null, page)
            else -> emptyList()
        }
    }
}