package com.example.cinesphere.data.dto

import com.google.gson.annotations.SerializedName

data class WatchProviderDto(
    @SerializedName("results")
    val results: Map<String, CountryProvidersDto>
)

data class CountryProvidersDto(
    @SerializedName("flatrate")
    val flatrate: List<ProviderDto>?
)

data class ProviderDto(
    @SerializedName("provider_name")
    val providerName: String
)