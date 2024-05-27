package com.example.entrega1.utils.schemas

data class Tour (
    val user: User = User(),
    val title: String = "",
    val description: String = "",
    var id: String = "",
    var approved: Boolean = false
)
