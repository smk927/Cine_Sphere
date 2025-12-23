package com.example.cinesphere.data.repository

import com.example.cinesphere.data.dto.toGenre
import com.example.cinesphere.data.dto.toMedia
import com.example.cinesphere.data.dto.toMovie
import com.example.cinesphere.data.dto.toWebSeries
import com.example.cinesphere.data.local.MovieDao
import com.example.cinesphere.data.remote.MovieApi
import com.example.cinesphere.domain.model.Genre
import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.model.MediaType
import com.example.cinesphere.domain.model.Movie
import com.example.cinesphere.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDao: MovieDao
) : MediaRepository {

    private var movieGenres: List<Genre> = emptyList()
    private var tvGenres: List<Genre> = emptyList()

    override suspend fun search(query: String, page: Int): List<Media> {
        val movieGenres = getMovieGenres()
        val tvGenres = getTvGenres()
        val allGenres = movieGenres + tvGenres
        return movieApi.search(apiKey = "YOUR_API_KEY", query = query, page = page).results.map { it.toMedia(allGenres) }
    }

    override suspend fun getPopularMedia(page: Int, mediaType: MediaType): List<Media> {
        return when (mediaType) {
            MediaType.MOVIE -> {
                val genres = getMovieGenres()
                movieApi.getPopularMovies(apiKey = "YOUR_API_KEY", page = page).results.map { it.toMovie(genres) }
            }
            MediaType.WEB_SERIES -> {
                val genres = getTvGenres()
                movieApi.getPopularWebSeries(apiKey = "YOUR_API_KEY", page = page).results.map { it.toWebSeries(genres) }
            }
        }
    }

    override suspend fun getTrendingMedia(): List<Media> {
        val genres = getMovieGenres()
        return movieApi.getTrendingMovies(apiKey = "YOUR_API_KEY").results.map { it.toMovie(genres) }
    }

    override suspend fun getMoviesByGenre(genreId: Int): List<Media> {
        val genres = getMovieGenres()
        return movieApi.getMoviesByGenre(apiKey = "YOUR_API_KEY", genreId = genreId).results.map { it.toMovie(genres) }
    }

    override suspend fun getMovieGenres(): List<Genre> {
        if (movieGenres.isEmpty()) {
            movieGenres = movieApi.getMovieGenres(apiKey = "YOUR_API_KEY").genres.map { it.toGenre() }
        }
        return movieGenres
    }

    override suspend fun getTvGenres(): List<Genre> {
        if (tvGenres.isEmpty()) {
            tvGenres = movieApi.getTvGenres(apiKey = "YOUR_API_KEY").genres.map { it.toGenre() }
        }
        return tvGenres
    }

    override suspend fun getMediaDetails(mediaId: Int, mediaType: MediaType): Media {
        return when (mediaType) {
            MediaType.MOVIE -> movieApi.getMovieDetails(movieId = mediaId, apiKey = "YOUR_API_KEY").toMovie(getMovieGenres())
            MediaType.WEB_SERIES -> movieApi.getWebSeriesDetails(tvId = mediaId, apiKey = "YOUR_API_KEY").toWebSeries(getTvGenres())
        }
    }

    override suspend fun addToWishlist(movie: Movie) {
        movieDao.addToWishlist(movie)
    }

    override suspend fun removeFromWishlist(movie: Movie) {
        movieDao.removeFromWishlist(movie)
    }

    override fun getWishlist(): Flow<List<Movie>> {
        return movieDao.getWishlist()
    }

    override fun isWishlisted(id: Int): Flow<Boolean> {
        return movieDao.isWishlisted(id)
    }
}