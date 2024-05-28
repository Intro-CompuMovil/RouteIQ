package com.example.entrega1.utils.schemas

import org.osmdroid.util.GeoPoint

data class Location(
    var longitude : Double = 0.0,
    var latitude : Double = 0.0
) {

    fun getGeoPoint() : GeoPoint {
        return GeoPoint(latitude, longitude)
    }
}