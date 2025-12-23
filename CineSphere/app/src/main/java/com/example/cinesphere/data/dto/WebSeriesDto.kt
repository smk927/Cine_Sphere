package com.example.cinesphere.data.dto

import com.google.gson.annotations.SerializedName

data class WebSeriesDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("genre_ids")
    val genreIds: List<Int>,
    @SerializedName("media_type")
    val mediaType: String = "tv"
)