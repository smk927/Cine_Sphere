package com.example.cinesphere.domain.usecase

import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.model.MediaType
import com.example.cinesphere.domain.model.Movie
import com.example.cinesphere.domain.model.WebSeries
import com.example.cinesphere.domain.repository.MediaRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ToggleWishlistUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    suspend operator fun invoke(media: Media) {
        val movie = when (media) {
            is Movie -> media
            is WebSeries -> Movie(
                id = media.id,
                title = media.title,
                overview = media.overview,
                posterUrl = media.posterUrl,
                voteAverage = media.voteAverage,
                mediaType = MediaType.WEB_SERIES
            )
            else -> return
        }

        val isWishlisted = mediaRepository.isWishlisted(movie.id).first()
        if (isWishlisted) {
            mediaRepository.removeFromWishlist(movie)
        } else {
            mediaRepository.addToWishlist(movie)
        }
    }
}