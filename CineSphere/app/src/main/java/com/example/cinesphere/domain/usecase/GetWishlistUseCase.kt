package com.example.cinesphere.domain.usecase

import com.example.cinesphere.domain.model.Movie
import com.example.cinesphere.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWishlistUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    operator fun invoke(): Flow<List<Movie>> {
        return mediaRepository.getWishlist()
    }
}