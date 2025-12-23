package com.example.cinesphere.domain.usecase

import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.repository.MediaRepository
import javax.inject.Inject

class SearchMediaUseCase @Inject constructor(private val mediaRepository: MediaRepository) {

    suspend operator fun invoke(query: String, page: Int): List<Media> {
        if (query.isBlank()) {
            return emptyList()
        }

        val lowerCaseQuery = query.lowercase()

        val mediaType = when {
            "movie" in lowerCaseQuery || "film" in lowerCaseQuery -> "movie"
            "tv" in lowerCaseQuery || "show" in lowerCaseQuery || "series" in lowerCaseQuery -> "tv"
            else -> null
        }

        val actorKeywords = listOf("with", "starring")
        val genreKeywords = listOf("about", "based on", "genre")

        var actorName: String? = null
        var genreName: String? = null
        var queryForSearch = " $lowerCaseQuery "

        for (keyword in actorKeywords) {
            if (" $keyword " in queryForSearch) {
                actorName = queryForSearch.substringAfter(" $keyword ").substringBefore(" about ").substringBefore(" based on ").substringBefore(" genre ")
                queryForSearch = queryForSearch.replace(" $keyword $actorName", " ")
                break
            }
        }

        for (keyword in genreKeywords) {
            if (" $keyword " in queryForSearch) {
                genreName = queryForSearch.substringAfter(" $keyword ")
                queryForSearch = queryForSearch.replace(" $keyword $genreName", " ")
                break
            }
        }

        if (genreName == null) {
            val allGenres = (mediaRepository.getMovieGenres() + mediaRepository.getTvGenres()).distinctBy { it.name.lowercase() }
            val foundGenre = allGenres.firstOrNull { genre -> queryForSearch.contains(" ${genre.name.lowercase()} ") }
            if (foundGenre != null) {
                genreName = foundGenre.name
            }
        }

        val hasAdvancedTerms = actorName != null || genreName != null

        return if (hasAdvancedTerms) {
            val actorId = actorName?.let { mediaRepository.searchPerson(it.trim()) }
            val genreId = genreName?.let { name ->
                val genres = if (mediaType == "tv") mediaRepository.getTvGenres() else mediaRepository.getMovieGenres()
                genres.firstOrNull { it.name.equals(name.trim(), ignoreCase = true) }?.id
            }

            if (actorId == null && genreId == null) {
                return mediaRepository.search(query, page)
            }

            if (mediaType == "tv") {
                mediaRepository.discoverTv(genreId?.toString(), actorId?.toString(), page)
            } else {
                mediaRepository.discoverMovies(genreId?.toString(), actorId?.toString(), page)
            }
        } else {
            mediaRepository.search(query, page)
        }
    }
}
