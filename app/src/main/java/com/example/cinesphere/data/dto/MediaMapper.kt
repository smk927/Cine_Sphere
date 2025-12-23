package com.example.cinesphere.data.dto

import com.example.cinesphere.domain.model.Genre
import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.model.Movie
import com.example.cinesphere.domain.model.WebSeries

fun MediaDto.toMedia(genres: List<Genre>): Media {
    val mediaGenres = genreIds.mapNotNull { id -> genres.find { it.id == id } }
    return when (mediaType) {
        "movie" -> Movie(
            id = id,
            title = title ?: "",
            overview = overview,
            posterUrl = "https://image.tmdb.org/t/p/w500" + posterPath,
            voteAverage = voteAverage,
            genres = mediaGenres
        )
        "tv" -> WebSeries(
            id = id,
            title = name ?: "",
            overview = overview,
            posterUrl = "https://image.tmdb.org/t/p/w500" + posterPath,
            voteAverage = voteAverage,
            genres = mediaGenres
        )
        else -> throw IllegalArgumentException("Unknown media type: $mediaType")
    }
}
