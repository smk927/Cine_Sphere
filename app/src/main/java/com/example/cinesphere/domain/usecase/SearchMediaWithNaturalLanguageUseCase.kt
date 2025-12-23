package com.example.cinesphere.domain.usecase

import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.repository.MediaRepository
import javax.inject.Inject

class SearchMediaWithNaturalLanguageUseCase @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val searchMediaUseCase: SearchMediaUseCase
) {
    suspend operator fun invoke(query: String): List<Media> {
        val actorPattern = "(?:movies|films) (?:of|by|featuring|starring) (.+)".toRegex(RegexOption.IGNORE_CASE)
        val matchResult = actorPattern.find(query)

        return if (matchResult != null) {
            val actorName = matchResult.groupValues[1].trim()
            val personId = mediaRepository.searchPerson(actorName)
            if (personId != null) {
                // If actor found, discover movies with that cast ID
                mediaRepository.discoverMovies(null, personId.toString(), 1)
            } else {
                // Fallback to normal search if actor not found
                searchMediaUseCase(query, 1)
            }
        } else {
            // Normal search
            searchMediaUseCase(query, 1)
        }
    }
}