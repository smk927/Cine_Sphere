package com.example.cinesphere.data.dto

import com.example.cinesphere.domain.model.Genre
import com.example.cinesphere.domain.model.MediaType
import com.example.cinesphere.domain.model.WebSeries

fun WebSeriesDto.toWebSeries(genres: List<Genre>): WebSeries {
    val webSeriesGenres = genreIds.mapNotNull { id -> genres.find { it.id == id } }
    return WebSeries(
        id = id,
        title = name,
        overview = overview,
        posterUrl = "https://image.tmdb.org/t/p/w500$posterPath",
        voteAverage = voteAverage,
        mediaType = MediaType.WEB_SERIES,
        genres = webSeriesGenres
    )
}