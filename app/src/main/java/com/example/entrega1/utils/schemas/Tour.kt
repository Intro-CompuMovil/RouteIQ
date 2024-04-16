package com.example.entrega1.utils.schemas

data class Tour (
    val user: User,
    val title: String,
    val description: String,
    var id: Int,
    var approved: Boolean
)
