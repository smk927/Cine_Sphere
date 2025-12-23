package com.example.cinesphere.data.dto

import com.google.gson.annotations.SerializedName

data class GenreListDto(
    @SerializedName("genres")
    val genres: List<GenreDto>
)