package com.example.cinesphere.data.repository

import com.example.cinesphere.domain.model.Movie

interface MovieRepository {
    suspend fun getPopularMovies(): List<Movie>
}