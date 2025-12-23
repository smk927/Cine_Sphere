package com.example.cinesphere.domain.model

data class WebSeries(
    override var id: Int,
    override var title: String,
    override var overview: String,
    override var posterUrl: String,
    override var voteAverage: Double,
    override var mediaType: MediaType = MediaType.WEB_SERIES,
    override var status: String? = null,
    override var tagline: String? = null,
    override var ottPlatform: String? = null,
    var firstAirDate: String? = null,
    var lastAirDate: String? = null,
    var numberOfEpisodes: Int? = null,
    var numberOfSeasons: Int? = null,
    var genres: List<Genre> = emptyList()
) : Media