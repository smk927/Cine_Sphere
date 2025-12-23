package com.example.cinesphere.data.remote

import com.example.cinesphere.data.dto.GenreListDto
import com.example.cinesphere.data.dto.MediaListDto
import com.example.cinesphere.data.dto.MovieDto
import com.example.cinesphere.data.dto.MovieListDto
import com.example.cinesphere.data.dto.PersonListDto
import com.example.cinesphere.data.dto.WatchProviderDto
import com.example.cinesphere.data.dto.WebSeriesDto
import com.example.cinesphere.data.dto.WebSeriesListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("search/multi")
    suspend fun search(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): MediaListDto

    @GET("search/person")
    suspend fun searchPerson(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): PersonListDto

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): MovieListDto

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): MovieListDto

    @GET("discover/tv")
    suspend fun getAnime(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreIds: String = "16",
        @Query("page") page: Int
    ): WebSeriesListDto

    @GET("trending/movie/day")
    suspend fun getTrendingMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): MovieListDto

    @GET("discover/movie")
    suspend fun discoverMovie(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreIds: String?,
        @Query("with_cast") castIds: String?,
        @Query("page") page: Int
    ): MovieListDto

    @GET("discover/tv")
    suspend fun discoverTv(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreIds: String?,
        @Query("with_cast") castIds: String?,
        @Query("page") page: Int
    ): WebSeriesListDto

    @GET("genre/movie/list")
    suspend fun getMovieGenres(@Query("api_key") apiKey: String): GenreListDto

    @GET("genre/tv/list")
    suspend fun getTvGenres(@Query("api_key") apiKey: String): GenreListDto

    @GET("tv/popular")
    suspend fun getPopularWebSeries(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): WebSeriesListDto

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): MovieDto

    @GET("tv/{tv_id}")
    suspend fun getWebSeriesDetails(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String
    ): WebSeriesDto

    @GET("movie/{movie_id}/watch/providers")
    suspend fun getMovieWatchProviders(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): WatchProviderDto

    @GET("tv/{tv_id}/watch/providers")
    suspend fun getTvWatchProviders(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String
    ): WatchProviderDto
}