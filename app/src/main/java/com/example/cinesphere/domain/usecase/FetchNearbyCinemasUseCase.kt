package com.example.cinesphere.domain.usecase

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.example.cinesphere.domain.model.Cinema
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FetchNearbyCinemasUseCase @Inject constructor(
    private val getNearbyCinemasUseCase: GetNearbyCinemasUseCase,
    @ApplicationContext private val context: Context
) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend operator fun invoke(radius: Int): Pair<LatLng?, List<Cinema>> {
        return try {
            val location: Location? = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY, null
            ).await()

            location?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                val cinemas = getNearbyCinemasUseCase(latLng, radius)
                Pair(latLng, cinemas)
            } ?: Pair(null, emptyList())
        } catch (e: Exception) {
            e.printStackTrace()
            Pair(null, emptyList())
        }
    }
}