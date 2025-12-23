package com.example.cinesphere.data.dto

import com.example.cinesphere.domain.model.Genre

fun GenreDto.toGenre(): Genre {
    return Genre(id = id, name = name)
}