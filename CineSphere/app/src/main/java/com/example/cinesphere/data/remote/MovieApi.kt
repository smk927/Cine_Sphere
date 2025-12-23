package com.example.cinesphere.data.remote

import com.example.cinesphere.data.dto.GenreListDto
import com.example.cinesphere.data.dto.MediaListDto
import com.example.cinesphere.data.dto.MovieDto
import com.example.cinesphere.data.dto.MovieListDto
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
    
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): MovieListDto

    @GET("trending/movie/day")
    suspend fun getTrendingMovies(
        @Query("api_key") apiKey: String
    ): MovieListDto

    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreId: Int
    ): MovieListDto

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
}