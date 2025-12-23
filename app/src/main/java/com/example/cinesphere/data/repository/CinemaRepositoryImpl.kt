package com.example.cinesphere.data.repository

import com.example.cinesphere.domain.model.Cinema
import com.example.cinesphere.domain.repository.CinemaRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import javax.inject.Inject

class CinemaRepositoryImpl @Inject constructor(
    private val client: OkHttpClient,
    private val apiKey: String
) : CinemaRepository {

    override suspend fun getNearbyCinemas(location: LatLng, radius: Int): List<Cinema> = withContext(Dispatchers.IO) {
        val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "location=${location.latitude},${location.longitude}" +
                "&radius=$radius" +
                "&type=movie_theater" +
                "&key=$apiKey"

        val request = Request.Builder().url(url).build()

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: return@withContext emptyList()
            
            // Log response body for debugging purposes if needed
            println("CinemaRepository: Request URL: $url")
            println("CinemaRepository: Response: $responseBody")

            val json = JSONObject(responseBody)
            
            // Check for error_message
            if (json.has("error_message")) {
                println("CinemaRepository: Error Message: ${json.getString("error_message")}")
            }
            if (json.has("status")) {
                println("CinemaRepository: Status: ${json.getString("status")}")
            }

            val results = json.optJSONArray("results") ?: return@withContext emptyList()
            
            val cinemas = mutableListOf<Cinema>()
            for (i in 0 until results.length()) {
                val place = results.getJSONObject(i)
                val geometry = place.getJSONObject("geometry").getJSONObject("location")
                val lat = geometry.getDouble("lat")
                val lng = geometry.getDouble("lng")
                val name = place.getString("name")
                val vicinity = place.optString("vicinity", "")
                val id = place.getString("place_id")
                
                cinemas.add(Cinema(id, name, LatLng(lat, lng), vicinity))
            }
            cinemas
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
