package com.example.cinesphere.di

import com.example.cinesphere.data.remote.MovieApi
import com.example.cinesphere.data.remote.TriviaApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named("TriviaRetrofit")
    fun provideTriviaRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://opentdb.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideTriviaApi(@Named("TriviaRetrofit") retrofit: Retrofit): TriviaApi {
        return retrofit.create(TriviaApi::class.java)
    }

    @Provides
    @Singleton
    @Named("MovieRetrofit")
    fun provideMovieRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieApi(@Named("MovieRetrofit") retrofit: Retrofit): MovieApi {
        return retrofit.create(MovieApi::class.java)
    }
}
