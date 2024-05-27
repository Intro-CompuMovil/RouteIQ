package com.example.entrega1.utils.schemas

import java.util.Date

data class Offer (
    val agency: Agency = Agency("", ""),
    val amount: Double = 0.0,
    val comments: String = "",
    val date : Date = Date(),
    val tourId : String = "",
    val places: ArrayList<Place> = ArrayList(),
    var id: String = "",
    var accepted: Boolean = false
)