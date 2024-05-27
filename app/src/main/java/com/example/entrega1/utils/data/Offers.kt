package com.example.entrega1.utils.data

import android.util.Log
import com.example.entrega1.utils.schemas.Agency
import com.example.entrega1.utils.schemas.Offer
import com.example.entrega1.utils.schemas.Tour
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Date

class Offers {

    companion object {
        var offers : ArrayList<Offer> = ArrayList()
        val db : RealTimeCRUD = RealTimeCRUD()
        fun addOffer(newOffer: Offer) {
            val key = db.getUniqueKey("offers")
            newOffer.id = key!!
            db.writeData("offers/${key}", newOffer) {
                if (it != null) {
                    offers.add(newOffer)
                }
            }
        }

        fun getOffers(callback: (ArrayList<Offer>?) -> Unit) {
            db.readData<HashMap<String, Offer>>("offers") {
                if (it != null) {
                    Log.i("OFFERS [GET OFFERS]", it.toString())
                    offers = it.map { it.value } as ArrayList<Offer>
                    callback(offers)
                } else callback(null)
            }
        }

        fun removeOffer(off: Offer) {
            offers.remove(off)
        }

        fun findById(id: String, callback: (Offer?) -> Unit) {
            db.readData<Offer>("offers/${id}", callback)
        }

        fun acceptOfferById(id: String) {
            db.writeData("offers/${id}/accepted", true) {}
        }
    }

}