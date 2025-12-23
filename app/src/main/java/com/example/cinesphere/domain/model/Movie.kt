package com.example.cinesphere.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist_movies")
data class Movie(
    @PrimaryKey
    override var id: Int = 0,
    override var title: String = "",
    override var overview: String = "",
    override var posterUrl: String = "",
    override var voteAverage: Double = 0.0,
    override var mediaType: MediaType = MediaType.MOVIE,
    override var status: String? = null,
    override var tagline: String? = null,
    override var ottPlatform: String? = null,
    var releaseDate: String? = null,
    var runtime: Int? = null,
    var genres: List<Genre> = emptyList()
) : Media