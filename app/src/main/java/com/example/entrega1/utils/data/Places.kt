package com.example.entrega1.utils.data

import com.example.entrega1.utils.schemas.Place
import com.example.entrega1.utils.schemas.User

class Places {

    companion object PlacesManager {
        private val userPlaces : MutableMap<String, HashSet<Place>> = mutableMapOf()

        fun addPlaceUser(user: User, place: Place) {
            if (userPlaces[user.email!!] == null) userPlaces[user.email!!] = HashSet()

            userPlaces[user.email]!!.add(place)
        }

        fun printPlaces() : String {
            return userPlaces.toString()
        }

        fun getPlacesFromUser(user: User) : HashSet<Place>? {
            return userPlaces[user.email!!]
        }
    }

}