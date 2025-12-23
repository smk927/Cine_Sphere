package com.example.cinesphere.data.dto

import com.google.gson.annotations.SerializedName

data class MediaListDto(
    @SerializedName("results")
    val results: List<MediaDto>
)
