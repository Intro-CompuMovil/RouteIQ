package com.example.entrega1.utils.data

import com.example.entrega1.utils.schemas.Place
import org.osmdroid.util.GeoPoint

class PlacesRender {

    companion object {
        private var placesToRender : ArrayList<Place> = ArrayList()

        fun addPlace(place: Place) {
            placesToRender.add(place)
        }

        fun deletePlace(place: Place) {
            placesToRender.remove(place)
        }

        fun clearPlaces() {
            placesToRender.clear()
        }

        fun printPlaces() : String {
            return placesToRender.toString()
        }

        fun getGeopointsPlaces() : ArrayList<GeoPoint> {
            return placesToRender.map { it.location.getGeoPoint() } as ArrayList<GeoPoint>
        }

        fun getPlaces() : ArrayList<Place> {
            return placesToRender.clone() as ArrayList<Place>
        }

        fun setPlaces(places: ArrayList<Place>) {
            placesToRender = places
        }

        fun getPlacesUids(): ArrayList<String> {
            return placesToRender.map { it -> it.firebaseUid } as ArrayList<String>
        }
    }

}