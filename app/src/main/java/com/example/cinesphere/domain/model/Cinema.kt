package com.example.cinesphere.domain.model

import com.google.android.gms.maps.model.LatLng

data class Cinema(
    val id: String,
    val name: String,
    val location: LatLng,
    val vicinity: String
)
