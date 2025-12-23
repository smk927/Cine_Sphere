package com.example.cinesphere.data.dto

import com.google.gson.annotations.SerializedName

data class WebSeriesListDto(
    @SerializedName("results")
    val results: List<WebSeriesDto>
)