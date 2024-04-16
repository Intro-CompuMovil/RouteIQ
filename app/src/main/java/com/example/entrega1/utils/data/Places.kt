package com.example.entrega1.utils.data

import com.example.entrega1.utils.schemas.Place
import com.example.entrega1.utils.schemas.User

class Places {

    companion object PlacesMannager {
        private val userPlaces : HashMap<String, HashSet<Place>> = HashMap()

        fun addPlaceUser(user: User, place: Place) {
            userPlaces[user.email]?.add(place)
        }

        fun printPlaces() : String {
            return userPlaces.toString()
        }
    }

}