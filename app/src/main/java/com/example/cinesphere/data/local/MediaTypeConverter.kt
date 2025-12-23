package com.example.cinesphere.data.local

import androidx.room.TypeConverter
import com.example.cinesphere.domain.model.MediaType

class MediaTypeConverter {
    @TypeConverter
    fun fromMediaType(mediaType: MediaType): String {
        return mediaType.name
    }

    @TypeConverter
    fun toMediaType(name: String): MediaType {
        return MediaType.valueOf(name)
    }
}
