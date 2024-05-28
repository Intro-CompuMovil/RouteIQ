package com.example.entrega1.utils.data

import android.net.Uri
import com.example.entrega1.utils.schemas.Place
import com.example.entrega1.utils.schemas.User

class Places {

    companion object PlacesManager {
        private val userPlaces : MutableMap<String, HashSet<Place>> = mutableMapOf()
        private val db: RealTimeCRUD = RealTimeCRUD()
        private val imageStorage: StorageCRUD = StorageCRUD()
        fun addPlaceUser(user: User, place: Place, imageUri: Uri?) {
            val key = db.getUniqueKey("places/${user.firebaseUid}")
            place.firebaseUid = key!!
            if (imageUri !=  null) {
                imageStorage.uploadFile("images/$key", imageUri) {}
            }
            db.writeData("places/${user.firebaseUid}/${key}", place) {}
        }

        fun printPlaces() : String {
            return userPlaces.toString()
        }

        fun getPlacesFromUser(user: User, callback: (ArrayList<Place>?) -> Unit) {
            db.readData< HashMap<String, Place> >("places/${user.firebaseUid}") {
                if (it != null) {
                    callback( it.map { element -> element.value } as ArrayList<Place> )
                } else {
                    callback(null)
                }
            }
        }

        fun getPlacesFromUser(userUid: String, callback: (ArrayList<Place>?) -> Unit) {
            db.readData< HashMap<String, Place> >("places/${userUid}") {
                if (it != null) {
                    callback( it.map { element -> element.value } as ArrayList<Place> )
                } else {
                    callback(null)
                }
            }
        }
    }

}