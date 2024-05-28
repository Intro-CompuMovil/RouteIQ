package com.example.entrega1.utils.schemas

import android.graphics.Bitmap
import android.net.Uri

data class Place(
    var firebaseUid : String? = "",
    val location: Location = Location(0.0, 0.0),
    val title: String = "",
    val locationDescription: String = "",
    val description: String = "",
    var hasImage: Boolean = false
)
