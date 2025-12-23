package com.example.cinesphere.data.repository

import com.example.cinesphere.BuildConfig
import com.example.cinesphere.data.dto.toGenre
import com.example.cinesphere.data.dto.toMovie
import com.example.cinesphere.data.dto.toOttPlatform
import com.example.cinesphere.data.dto.toWebSeries
import com.example.cinesphere.data.local.MovieDao
import com.example.cinesphere.data.remote.MovieApi
import com.example.cinesphere.domain.model.Genre
import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.model.MediaType
import com.example.cinesphere.domain.model.Movie
import com.example.cinesphere.domain.model.WebSeries
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
        return movieApi.search(apiKey = BuildConfig.API_KEY, query = query, page = page).results.mapNotNull { mediaDto ->
            when (mediaDto.mediaType) {
                "movie" -> {
                    val genres = mediaDto.genreIds?.mapNotNull { id -> movieGenres.find { it.id == id } } ?: emptyList()
                    Movie(
                        id = mediaDto.id,
                        title = mediaDto.title ?: mediaDto.name ?: "",
                        overview = mediaDto.overview ?: "",
                        posterUrl = mediaDto.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
                        voteAverage = mediaDto.voteAverage,
                        mediaType = MediaType.MOVIE,
                        genres = genres
                    )
                }
                "tv" -> {
                    val genres = mediaDto.genreIds?.mapNotNull { id -> tvGenres.find { it.id == id } } ?: emptyList()
                    WebSeries(
                        id = mediaDto.id,
                        title = mediaDto.name ?: mediaDto.title ?: "",
                        overview = mediaDto.overview ?: "",
                        posterUrl = mediaDto.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
                        voteAverage = mediaDto.voteAverage,
                        mediaType = MediaType.WEB_SERIES,
                        genres = genres
                    )
                }
                else -> null
            }
        }
    }

    override suspend fun searchPerson(query: String): Int? {
        return movieApi.searchPerson(apiKey = BuildConfig.API_KEY, query = query, page = 1).results.firstOrNull()?.id
    }

    override suspend fun getPopularMedia(page: Int, mediaType: MediaType): List<Media> {
        return when (mediaType) {
            MediaType.MOVIE -> {
                val genres = getMovieGenres()
                movieApi.getPopularMovies(apiKey = BuildConfig.API_KEY, page = page).results.map { it.toMovie(genres) }
            }
            MediaType.WEB_SERIES -> {
                val genres = getTvGenres()
                movieApi.getPopularWebSeries(apiKey = BuildConfig.API_KEY, page = page).results.map { it.toWebSeries(genres) }
            }
        }
    }

    override suspend fun getTrendingMedia(page: Int): List<Media> {
        val genres = getMovieGenres()
        return movieApi.getTrendingMovies(apiKey = BuildConfig.API_KEY, page = page).results.map { it.toMovie(genres) }
    }

    override suspend fun getUpcomingMovies(page: Int): List<Media> {
        val genres = getMovieGenres()
        return movieApi.getUpcomingMovies(apiKey = BuildConfig.API_KEY, page = page).results.map { it.toMovie(genres) }
    }

    override suspend fun getAnime(page: Int): List<Media> {
        val genres = getTvGenres()
        return movieApi.getAnime(apiKey = BuildConfig.API_KEY, page = page).results.map { it.toWebSeries(genres) }
    }

    override suspend fun getMoviesByGenre(genreId: Int): List<Media> {
        val genres = getMovieGenres()
        return movieApi.discoverMovie(apiKey = BuildConfig.API_KEY, genreIds = genreId.toString(), castIds = null, page = 1).results.map { it.toMovie(genres) }
    }

    override suspend fun discoverMovies(genreIds: String?, castIds: String?, page: Int): List<Media> {
        val genres = getMovieGenres()
        return movieApi.discoverMovie(apiKey = BuildConfig.API_KEY, genreIds = genreIds, castIds = castIds, page = page).results.map { it.toMovie(genres) }
    }

    override suspend fun discoverTv(genreIds: String?, castIds: String?, page: Int): List<Media> {
        val genres = getTvGenres()
        return movieApi.discoverTv(apiKey = BuildConfig.API_KEY, genreIds = genreIds, castIds = castIds, page = page).results.map { it.toWebSeries(genres) }
    }

    override suspend fun getMovieGenres(): List<Genre> {
        if (movieGenres.isEmpty()) {
            movieGenres = movieApi.getMovieGenres(apiKey = BuildConfig.API_KEY).genres.map { it.toGenre() }
        }
        return movieGenres
    }

    override suspend fun getTvGenres(): List<Genre> {
        if (tvGenres.isEmpty()) {
            tvGenres = movieApi.getTvGenres(apiKey = BuildConfig.API_KEY).genres.map { it.toGenre() }
        }
        return tvGenres
    }

    override suspend fun getMediaDetails(mediaId: Int, mediaType: MediaType): Media {
        return when (mediaType) {
            MediaType.MOVIE -> {
                val movie = movieApi.getMovieDetails(movieId = mediaId, apiKey = BuildConfig.API_KEY).toMovie(getMovieGenres())
                val watchProviders = movieApi.getMovieWatchProviders(movieId = mediaId, apiKey = BuildConfig.API_KEY)
                movie.ottPlatform = watchProviders.toOttPlatform()
                movie
            }
            MediaType.WEB_SERIES -> {
                val webSeries = movieApi.getWebSeriesDetails(tvId = mediaId, apiKey = BuildConfig.API_KEY).toWebSeries(getTvGenres())
                val watchProviders = movieApi.getTvWatchProviders(tvId = mediaId, apiKey = BuildConfig.API_KEY)
                webSeries.ottPlatform = watchProviders.toOttPlatform()
                webSeries
            }
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
