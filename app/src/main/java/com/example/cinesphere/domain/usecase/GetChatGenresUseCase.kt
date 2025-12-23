package com.example.cinesphere.domain.usecase

import com.example.cinesphere.data.repository.GenreChatRepository
import com.example.cinesphere.domain.model.GenreRoom
import javax.inject.Inject

class GetChatGenresUseCase @Inject constructor(
    private val repository: GenreChatRepository
) {
    suspend operator fun invoke(): Pair<List<GenreRoom>, List<GenreRoom>> {
        val allGenres = repository.getAvailableGenres()
        val joinedGenreIds = repository.getJoinedGenres()
        
        val joinedGenres = allGenres.filter { it.id in joinedGenreIds }
        val availableGenres = allGenres.filter { it.id !in joinedGenreIds }
        
        return Pair(joinedGenres, availableGenres)
    }
}