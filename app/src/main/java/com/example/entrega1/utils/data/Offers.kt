package com.example.entrega1.utils.data

import android.util.Log
import com.example.entrega1.utils.schemas.Offer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

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

        fun watchOffers(callback: (ArrayList<Offer>?) -> Unit) {
            db.watchData("offers", object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val offers = snapshot.getValue<HashMap<String, Offer>>()?.map { it.value } as ArrayList<Offer>
                    callback(offers)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(null)
                }

            })
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