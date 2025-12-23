package com.example.cinesphere.domain.model

interface Media {
    var id: Int
    var title: String
    var overview: String
    var posterUrl: String
    var voteAverage: Double
    var mediaType: MediaType
}