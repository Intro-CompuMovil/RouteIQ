package com.example.entrega1.utils.schemas

import android.os.Parcel
import android.os.Parcelable

data class User (
    val firebaseUid: String? = "",
    var email: String? = "",
    var name: String? = "",
    val type: String? = ""
)
