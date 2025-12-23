package com.example.cinesphere.domain.usecase

import com.example.cinesphere.data.repository.GenreChatRepository
import javax.inject.Inject

class JoinGenreUseCase @Inject constructor(
    private val repository: GenreChatRepository
) {
    suspend operator fun invoke(genreId: String) {
        repository.joinGenre(genreId)
    }
}