package com.example.cinesphere.presentation.map

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinesphere.domain.model.Cinema
import com.example.cinesphere.domain.usecase.FetchNearbyCinemasUseCase
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val fetchNearbyCinemasUseCase: FetchNearbyCinemasUseCase
) : ViewModel() {

    private val _userLocation = MutableStateFlow<LatLng?>(null)
    val userLocation: StateFlow<LatLng?> = _userLocation.asStateFlow()

    private val _cinemas = MutableStateFlow<List<Cinema>>(emptyList())
    val cinemas: StateFlow<List<Cinema>> = _cinemas.asStateFlow()

    @SuppressLint("MissingPermission")
    fun fetchLocationAndCinemas() {
        viewModelScope.launch {
            val (location, cinemas) = fetchNearbyCinemasUseCase(10000)
            _userLocation.value = location
            _cinemas.value = cinemas
        }
    }
}