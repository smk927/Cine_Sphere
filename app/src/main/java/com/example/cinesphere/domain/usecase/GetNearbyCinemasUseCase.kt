package com.example.cinesphere.domain.usecase

import com.example.cinesphere.domain.model.Cinema
import com.example.cinesphere.domain.repository.CinemaRepository
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class GetNearbyCinemasUseCase @Inject constructor(
    private val cinemaRepository: CinemaRepository
) {
    suspend operator fun invoke(location: LatLng, radius: Int): List<Cinema> {
        return try {
            cinemaRepository.getNearbyCinemas(location, radius)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}