package com.example.cinesphere.domain.usecase

import com.example.cinesphere.domain.model.Movie
import com.example.cinesphere.domain.repository.MediaRepository
import javax.inject.Inject

class AddToWishlistUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    suspend operator fun invoke(movie: Movie) {
        mediaRepository.addToWishlist(movie)
    }
}