package com.example.entrega1.utils.schemas

import java.util.Date

data class Offer (
    val agency: Agency,
    val amount: Double,
    val comments: String,
    val date : Date,
    val tourId : Int,
    val places: ArrayList<Place>,
    var id: Int,
    var accepted: Boolean
)