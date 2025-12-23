package com.example.cinesphere.domain.model

data class WebSeries(
    override var id: Int,
    override var title: String,
    override var overview: String,
    override var posterUrl: String,
    override var voteAverage: Double,
    override var mediaType: MediaType = MediaType.WEB_SERIES,
    var genres: List<Genre> = emptyList()
) : Media
