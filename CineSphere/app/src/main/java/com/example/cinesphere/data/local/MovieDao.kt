package com.example.cinesphere.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cinesphere.domain.model.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWishlist(movie: Movie)

    @Delete
    suspend fun removeFromWishlist(movie: Movie)

    @Query("SELECT * FROM wishlist_movies")
    fun getWishlist(): Flow<List<Movie>>

    @Query("SELECT EXISTS(SELECT * FROM wishlist_movies WHERE id = :id)")
    fun isWishlisted(id: Int): Flow<Boolean>
}