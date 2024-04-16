package com.example.entrega1.utils.data

import com.example.entrega1.utils.schemas.Agency
import com.example.entrega1.utils.schemas.Offer
import java.util.Date

class Offers {

    companion object {
        val offers : ArrayList<Offer> = ArrayList()

        fun addOffer(newOffer: Offer) {
            newOffer.id = offers.size
            offers.add(newOffer)
        }

        fun removeOffer(off: Offer) {
            offers.remove(off)
        }

        fun findById(id: Int) : Offer? {
            return offers.find { it.id == id }
        }

        fun acceptOfferById(id: Int) {
            offers.find { it.id == id }?.accepted = true
        }
    }

}