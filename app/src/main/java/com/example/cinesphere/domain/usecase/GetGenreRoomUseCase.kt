package com.example.cinesphere.domain.usecase

import com.example.cinesphere.data.repository.GenreChatRepository
import com.example.cinesphere.domain.model.GenreRoom
import javax.inject.Inject

class GetGenreRoomUseCase @Inject constructor(
    private val repository: GenreChatRepository
) {
    operator fun invoke(genreId: String): GenreRoom? {
        return repository.getGenreRoom(genreId)
    }
}