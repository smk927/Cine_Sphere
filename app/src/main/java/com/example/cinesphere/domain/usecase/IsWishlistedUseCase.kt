package com.example.cinesphere.domain.usecase

import com.example.cinesphere.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsWishlistedUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    operator fun invoke(id: Int): Flow<Boolean> {
        return mediaRepository.isWishlisted(id)
    }
}