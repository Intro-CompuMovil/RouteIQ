package com.example.entrega1.utils.schemas

import android.os.Parcel
import android.os.Parcelable

data class User (
    val firebaseUid: String? = "",
    val email: String? = "",
    val name: String? = "",
    val type: String? = ""
)
