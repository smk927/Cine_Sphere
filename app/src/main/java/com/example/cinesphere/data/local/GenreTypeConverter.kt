package com.example.cinesphere.data.local

import androidx.room.TypeConverter
import com.example.cinesphere.domain.model.Genre
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GenreTypeConverter {
    @TypeConverter
    fun fromGenreList(genres: List<Genre>?): String? {
        if (genres == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Genre>>() {}.type
        return gson.toJson(genres, type)
    }

    @TypeConverter
    fun toGenreList(genresString: String?): List<Genre>? {
        if (genresString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Genre>>() {}.type
        return gson.fromJson(genresString, type)
    }
}
