package com.example.cinesphere.di

import android.app.Application
import androidx.room.Room
import com.example.cinesphere.data.local.MovieDatabase
import com.example.cinesphere.data.remote.MovieApi
import com.example.cinesphere.data.repository.MediaRepositoryImpl
import com.example.cinesphere.domain.repository.MediaRepository
import com.example.cinesphere.domain.usecase.AddToWishlistUseCase
import com.example.cinesphere.domain.usecase.GetMediaDetailsUseCase
import com.example.cinesphere.domain.usecase.GetMoviesByGenreUseCase
import com.example.cinesphere.domain.usecase.GetPopularMediaUseCase
import com.example.cinesphere.domain.usecase.GetTrendingMediaUseCase
import com.example.cinesphere.domain.usecase.GetWishlistUseCase
import com.example.cinesphere.domain.usecase.IsWishlistedUseCase
import com.example.cinesphere.domain.usecase.RemoveFromWishlistUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMovieApi(): MovieApi {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieDatabase(app: Application): MovieDatabase {
        return Room.databaseBuilder(
            app,
            MovieDatabase::class.java,
            "movie_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMediaRepository(movieApi: MovieApi, db: MovieDatabase): MediaRepository {
        return MediaRepositoryImpl(movieApi, db.movieDao())
    }

    @Provides
    @Singleton
    fun provideGetPopularMediaUseCase(repository: MediaRepository): GetPopularMediaUseCase {
        return GetPopularMediaUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetTrendingMediaUseCase(repository: MediaRepository): GetTrendingMediaUseCase {
        return GetTrendingMediaUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetMoviesByGenreUseCase(repository: MediaRepository): GetMoviesByGenreUseCase {
        return GetMoviesByGenreUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddToWishlistUseCase(repository: MediaRepository): AddToWishlistUseCase {
        return AddToWishlistUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRemoveFromWishlistUseCase(repository: MediaRepository): RemoveFromWishlistUseCase {
        return RemoveFromWishlistUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideIsWishlistedUseCase(repository: MediaRepository): IsWishlistedUseCase {
        return IsWishlistedUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetWishlistUseCase(repository: MediaRepository): GetWishlistUseCase {
        return GetWishlistUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetMediaDetailsUseCase(repository: MediaRepository): GetMediaDetailsUseCase {
        return GetMediaDetailsUseCase(repository)
    }
}