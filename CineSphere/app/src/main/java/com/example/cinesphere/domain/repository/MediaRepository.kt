package com.example.cinesphere.domain.repository

import com.example.cinesphere.domain.model.Genre
import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.model.MediaType
import com.example.cinesphere.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    suspend fun search(query: String, page: Int): List<Media>
    suspend fun getPopularMedia(page: Int, mediaType: MediaType): List<Media>
    suspend fun getTrendingMedia(): List<Media>
    suspend fun getMoviesByGenre(genreId: Int): List<Media>
    suspend fun getMovieGenres(): List<Genre>
    suspend fun getTvGenres(): List<Genre>
    suspend fun getMediaDetails(mediaId: Int, mediaType: MediaType): Media
    suspend fun addToWishlist(movie: Movie)
    suspend fun removeFromWishlist(movie: Movie)
    fun getWishlist(): Flow<List<Movie>>
    fun isWishlisted(id: Int): Flow<Boolean>
}