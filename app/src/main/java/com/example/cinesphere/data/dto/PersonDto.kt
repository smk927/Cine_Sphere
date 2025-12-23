package com.example.cinesphere.data.dto

import com.google.gson.annotations.SerializedName

data class PersonDto(
    val id: Int,
    val name: String,
    @SerializedName("profile_path")
    val profilePath: String?
)
