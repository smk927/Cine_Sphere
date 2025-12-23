package com.example.cinesphere.di

import android.app.Application
import androidx.room.Room
import com.example.cinesphere.data.local.MovieDatabase
import com.example.cinesphere.data.local.MovieDao
import com.example.cinesphere.data.repository.AuthRepositoryImpl
import com.example.cinesphere.data.repository.CinemaRepositoryImpl
import com.example.cinesphere.data.repository.MediaRepositoryImpl
import com.example.cinesphere.data.repository.TriviaRepositoryImpl
import com.example.cinesphere.domain.repository.AuthRepository
import com.example.cinesphere.domain.repository.CinemaRepository
import com.example.cinesphere.domain.repository.MediaRepository
import com.example.cinesphere.domain.repository.TriviaRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    // provideFirebaseFirestore is already provided in FirebaseModule

    @Provides
    @Singleton
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    @Singleton
    fun provideMediaRepository(impl: MediaRepositoryImpl): MediaRepository = impl

    @Provides
    @Singleton
    fun provideDatabase(app: Application): MovieDatabase {
        return Room.databaseBuilder(
            app,
            MovieDatabase::class.java,
            "cinesphere_db"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideMovieDao(db: MovieDatabase): MovieDao {
        return db.movieDao()
    }
    
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideCinemaRepository(
        client: OkHttpClient
    ): CinemaRepository {
        return CinemaRepositoryImpl(client, "AIzaSyC9ZYtarbUJvilzUE2WZF0QYPGuwlRUSlY")
    }

    @Provides
    @Singleton
    fun provideTriviaRepository(impl: TriviaRepositoryImpl): TriviaRepository = impl
}
