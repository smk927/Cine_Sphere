package com.example.cinesphere.domain.repository

import com.example.cinesphere.domain.model.Cinema
import com.google.android.gms.maps.model.LatLng

interface CinemaRepository {
    suspend fun getNearbyCinemas(location: LatLng, radius: Int = 5000): List<Cinema>
}
