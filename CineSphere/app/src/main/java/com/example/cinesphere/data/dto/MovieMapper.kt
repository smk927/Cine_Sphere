package com.example.cinesphere.data.dto

import com.example.cinesphere.domain.model.Genre
import com.example.cinesphere.domain.model.MediaType
import com.example.cinesphere.domain.model.Movie

fun MovieDto.toMovie(genres: List<Genre>): Movie {
    val movieGenres = genreIds.mapNotNull { id -> genres.find { it.id == id } }
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterUrl = "https://image.tmdb.org/t/p/w500$posterPath",
        voteAverage = voteAverage,
        mediaType = MediaType.MOVIE
    ).apply { this.genres = movieGenres }
}