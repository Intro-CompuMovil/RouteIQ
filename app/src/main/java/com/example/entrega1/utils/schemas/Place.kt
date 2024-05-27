package com.example.entrega1.utils.schemas

import android.graphics.Bitmap
import org.osmdroid.util.GeoPoint

data class Place(
    val location: GeoPoint = GeoPoint(0.0, 0.0),
    val title: String = "",
    val locationDescription: String = "",
    val description: String = "",
    val bitmap: Bitmap? = null
)
